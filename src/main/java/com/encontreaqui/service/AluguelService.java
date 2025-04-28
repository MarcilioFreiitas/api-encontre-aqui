package com.encontreaqui.service;

import com.encontreaqui.dto.AluguelDTO;
import com.encontreaqui.mapper.AluguelMapper;
import com.encontreaqui.model.Aluguel;
import com.encontreaqui.model.Foto;
import com.encontreaqui.model.Usuario;
import com.encontreaqui.repository.AluguelRepository;
import com.encontreaqui.repository.UsuarioRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for rental advertisement operations including creation, listing,
 * updating (with image management) and deletion.
 */
@Service
@Transactional
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;  // Injetado para buscar o usuário

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Diretório fixo para armazenar as imagens – ajuste conforme necessário.
    private static final String UPLOAD_DIR = "uploads";

    // BASE_URL para compor a URL completa das imagens
    private static final String BASE_URL = "http://localhost:8080";

    // Cria um novo anúncio de aluguel (sem alteração para imagens)
    public AluguelDTO criarAluguel(AluguelDTO aluguelDTO) {
        Aluguel aluguel = AluguelMapper.INSTANCE.toEntity(aluguelDTO);

        // Verifica se o usuarioId foi enviado e associa o usuário à entidade
        if (aluguelDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(aluguelDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            aluguel.setUsuario(usuario);
        } else {
            throw new RuntimeException("Campo usuarioId é obrigatório.");
        }

        // Se houver fotos, garante que elas estejam associadas ao aluguel
        if (aluguel.getFotos() != null) {
            aluguel.getFotos().forEach(foto -> foto.setAluguel(aluguel));
        }

        // As datas não foram definidas no modelo original – mantendo comportamento anterior
        Aluguel salvo = aluguelRepository.save(aluguel);
        return AluguelMapper.INSTANCE.toDTO(salvo);
    }

    // Lista todos os anúncios de aluguel
    @Transactional(readOnly = true)
    public List<AluguelDTO> listarAlugueis() {
        return aluguelRepository.findAll().stream()
                .map(AluguelMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    // Busca um anúncio de aluguel pelo ID
    @Transactional(readOnly = true)
    public AluguelDTO buscarPorId(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));
        return AluguelMapper.INSTANCE.toDTO(aluguel);
    }

    /**
     * Atualiza um anúncio de aluguel existente, permitindo editar os campos e gerenciar as imagens
     * (removendo as existentes e adicionando novas).
     *
     * @param id                   ID do anúncio a ser atualizado.
     * @param aluguelDTO           Dados atualizados do aluguel (exceto imagens).
     * @param novasFotos           Array de novos arquivos das imagens (opcional).
     * @param fotosExistentesJson  String JSON representando a lista de caminhos das imagens a serem mantidas (opcional).
     * @return Dados do anúncio atualizado.
     */
    @Transactional
    public AluguelDTO atualizarAluguel(Long id, AluguelDTO aluguelDTO, MultipartFile[] novasFotos, String fotosExistentesJson) {
        Aluguel aluguelExistente = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));

        // Converte o DTO em entidade para capturar os campos atualizados
        Aluguel aluguelAtualizado = AluguelMapper.INSTANCE.toEntity(aluguelDTO);
        aluguelAtualizado.setId(aluguelExistente.getId());
        // NÃO chamamos setDataAtualizacao ou getDataCriacao, pois esses métodos não existem para Aluguel

        // Preserva a associação com o usuário (se o DTO contiver um novo usuarioId, busca; senão, mantém o existente)
        if (aluguelDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(aluguelDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            aluguelAtualizado.setUsuario(usuario);
        } else {
            aluguelAtualizado.setUsuario(aluguelExistente.getUsuario());
        }

        // Processa as imagens existentes: converte o JSON para uma lista de Strings (caminhos)
        final List<String> fotosExistentesFinal;
        try {
            if (fotosExistentesJson != null && !fotosExistentesJson.isEmpty()) {
                fotosExistentesFinal = objectMapper.readValue(fotosExistentesJson, new TypeReference<List<String>>() {});
            } else {
                fotosExistentesFinal = new ArrayList<>();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar fotos existentes.", e);
        }

        // Obtém as imagens já cadastradas e filtra aquelas que devem ser mantidas
        List<Foto> fotosAtuais = aluguelExistente.getFotos() != null ? aluguelExistente.getFotos() : new ArrayList<>();
        List<Foto> fotosMantidas = fotosAtuais.stream()
                .filter(f -> fotosExistentesFinal.contains(f.getCaminho()))
                .collect(Collectors.toList());

        // Processa as novas imagens, se houver
        if (novasFotos != null) {
            for (MultipartFile file : novasFotos) {
                if (!file.isEmpty()) {
                    // Salva o arquivo utilizando a lógica de armazenamento
                    String caminhoSalvo = saveFile(file);
                    Foto novaFoto = new Foto();
                    novaFoto.setCaminho(caminhoSalvo);
                    novaFoto.setAluguel(aluguelAtualizado);
                    fotosMantidas.add(novaFoto);
                }
            }
        }

        // Associa as imagens (mantidas e novas) ao anúncio de aluguel
        aluguelAtualizado.setFotos(fotosMantidas);

        Aluguel salvo = aluguelRepository.save(aluguelAtualizado);
        return AluguelMapper.INSTANCE.toDTO(salvo);
    }

    /**
     * Salva o arquivo enviado no diretório fixo definido por UPLOAD_DIR e retorna a URL completa.
     *
     * @param file O arquivo a ser salvo.
     * @return A URL completa do arquivo salvo, por exemplo: "http://localhost:8080/uploads/123456789_filename.jpg"
     */
    private String saveFile(MultipartFile file) {
        try {
            // Garante que o diretório de uploads exista
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Gera um nome único para o arquivo usando o timestamp atual e o nome original
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // Obtém o caminho completo para salvar o arquivo
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            // Copia o conteúdo do arquivo para o diretório de uploads, substituindo se já existir
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Retorna a URL completa utilizando BASE_URL e UPLOAD_DIR com "/" fixo
            return BASE_URL + "/" + UPLOAD_DIR + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo: " + e.getMessage(), e);
        }
    }

    // Deleta um anúncio de aluguel
    public void deletarAluguel(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));
        aluguelRepository.delete(aluguel);
    }
}
