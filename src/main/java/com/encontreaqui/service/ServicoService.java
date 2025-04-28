package com.encontreaqui.service;

import com.encontreaqui.dto.ServicoDTO;
import com.encontreaqui.mapper.ServicoMapper;
import com.encontreaqui.model.Foto;
import com.encontreaqui.model.Servico;
import com.encontreaqui.model.Usuario;
import com.encontreaqui.repository.ServicoRepository;
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
 * Service responsible for service operations (anúncios de serviço) including creation,
 * listing, updating (with image management) and deletion.
 */
@Service
@Transactional
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;  // Injetado para buscar o usuário

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Diretório fixo para armazenar as imagens – ajuste conforme necessário.
    // Neste exemplo, usamos "uploads" no mesmo diretório da aplicação.
    private static final String UPLOAD_DIR = "uploads";
    
    // Definição da BASE_URL para montar o caminho completo da imagem
    private static final String BASE_URL = "http://localhost:8080";

    // Cria um novo serviço
    public ServicoDTO criarServico(ServicoDTO servicoDTO) {
        Servico servico = ServicoMapper.INSTANCE.toEntity(servicoDTO);

        // Associação do usuário deve ser definida com base no usuarioId do DTO
        if (servicoDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(servicoDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            servico.setUsuario(usuario);
        } else {
            throw new RuntimeException("Campo usuarioId é obrigatório.");
        }

        // Se houver fotos, associa cada foto ao serviço
        if (servico.getFotos() != null) {
            servico.getFotos().forEach(foto -> foto.setServico(servico));
        }

        // Define as datas de criação e atualização
        servico.setDataCriacao(new Date());
        servico.setDataAtualizacao(new Date());

        Servico salvo = servicoRepository.save(servico);
        return ServicoMapper.INSTANCE.toDTO(salvo);
    }

    // Lista todos os serviços
    @Transactional(readOnly = true)
    public List<ServicoDTO> listarServicos() {
        return servicoRepository.findAll().stream()
                .map(ServicoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    // Busca um serviço pelo ID
    @Transactional(readOnly = true)
    public ServicoDTO buscarPorId(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        return ServicoMapper.INSTANCE.toDTO(servico);
    }

    /**
     * Atualiza um serviço existente, permitindo editar os campos e gerenciar as imagens
     * (remover fotos existentes e adicionar novas fotos).
     *
     * @param id                   ID do serviço a ser atualizado.
     * @param servicoDTO           Dados atualizados do serviço (exceto fotos).
     * @param novasFotos           Array de arquivos para novas fotos (opcional).
     * @param fotosExistentesJson  JSON string representando a lista de caminhos das fotos existentes que deverão ser mantidas (opcional).
     * @return Dados atualizados do serviço.
     */
    @Transactional
    public ServicoDTO atualizarServico(Long id, ServicoDTO servicoDTO, MultipartFile[] novasFotos, String fotosExistentesJson) {
        Servico servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        
        // Converte o DTO em entidade para capturar os campos atualizados
        Servico servicoAtualizado = ServicoMapper.INSTANCE.toEntity(servicoDTO);
        servicoAtualizado.setId(servicoExistente.getId());
        servicoAtualizado.setDataCriacao(servicoExistente.getDataCriacao());
        servicoAtualizado.setDataAtualizacao(new Date());
        
        // Preserva a associação com o usuário – se o DTO contiver um usuarioId, busca; senão, mantém o existente
        if (servicoDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(servicoDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            servicoAtualizado.setUsuario(usuario);
        } else {
            servicoAtualizado.setUsuario(servicoExistente.getUsuario());
        }
        
        // Processa as fotos existentes: converte o JSON para uma lista de strings
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
        
        // Obtém as fotos já cadastradas e filtra aquelas que devem ser mantidas
        List<Foto> fotosAtuais = servicoExistente.getFotos() != null ? servicoExistente.getFotos() : new ArrayList<>();
        List<Foto> fotosMantidas = fotosAtuais.stream()
                .filter(f -> fotosExistentesFinal.contains(f.getCaminho()))
                .collect(Collectors.toList());
        
        // Processa as novas fotos, se houver
        if (novasFotos != null) {
            for (MultipartFile file : novasFotos) {
                if (!file.isEmpty()) {
                    // Salva o arquivo, utilizando a lógica de armazenamento no diretório fixo
                    String caminhoSalvo = saveFile(file);
                    Foto novaFoto = new Foto();
                    novaFoto.setCaminho(caminhoSalvo);
                    novaFoto.setServico(servicoAtualizado);
                    fotosMantidas.add(novaFoto);
                }
            }
        }
        
        // Associa as fotos (mantidas e novas) ao serviço
        servicoAtualizado.setFotos(fotosMantidas);
        
        Servico salvo = servicoRepository.save(servicoAtualizado);
        return ServicoMapper.INSTANCE.toDTO(salvo);
    }
    
    /**
     * Salva o arquivo enviado e retorna a URL completa do arquivo armazenado.
     * A implementação salva o arquivo em um diretório fixo definido por UPLOAD_DIR.
     *
     * @param file O arquivo a ser salvo.
     * @return A URL completa do arquivo salvo, por exemplo: "http://localhost:8080/uploads/123456789_original.jpg"
     */
    private String saveFile(MultipartFile file) {
        try {
            // Garante que o diretório de uploads exista
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Gera um nome único para o arquivo usando timestamp e o nome original
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            
            // Obtém o caminho completo para salvar o arquivo
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            
            // Copia o arquivo para o diretório de uploads, substituindo se já existir
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Retorna a URL completa utilizando BASE_URL e UPLOAD_DIR com separador "/"
            return BASE_URL + "/" + UPLOAD_DIR + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo: " + e.getMessage(), e);
        }
    }

    // Deleta um serviço
    public void deletarServico(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        servicoRepository.delete(servico);
    }
}
