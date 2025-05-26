package com.encontreaqui.service;

import com.encontreaqui.dto.AluguelDTO;
import com.encontreaqui.mapper.AluguelMapper;
import com.encontreaqui.model.Aluguel;
import com.encontreaqui.model.Avaliacao;
import com.encontreaqui.model.Foto;
import com.encontreaqui.model.Usuario;
import com.encontreaqui.repository.AluguelRepository;
import com.encontreaqui.repository.UsuarioRepository;
import com.encontreaqui.repository.AvaliacaoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AvaliacaoRepository avaliacaoRepository; // Será usado para buscar as avaliações

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String UPLOAD_DIR = "uploads";
    private static final String BASE_URL = "http://localhost:8080";

    /**
     * Criação de anúncio de aluguel
     */
    public AluguelDTO criarAluguel(AluguelDTO aluguelDTO) {
        Aluguel aluguel = AluguelMapper.INSTANCE.toEntity(aluguelDTO);

        if (aluguelDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(aluguelDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            aluguel.setUsuario(usuario);
        } else {
            throw new RuntimeException("Campo usuarioId é obrigatório.");
        }

        if (aluguel.getFotos() != null) {
            aluguel.getFotos().forEach(foto -> foto.setAluguel(aluguel));
        }

        Aluguel salvo = aluguelRepository.save(aluguel);
        AluguelDTO dto = AluguelMapper.INSTANCE.toDTO(salvo);
        // Como é um novo anúncio, não há avaliações, logo a média é 0
        dto.setMediaAvaliacoes(0.0);
        return dto;
    }

    /**
     * Listagem de anúncios de aluguel
     */
    @Transactional(readOnly = true)
    public List<AluguelDTO> listarAlugueis() {
        List<Aluguel> alugueis = aluguelRepository.findAll();
        List<AluguelDTO> dtos = alugueis.stream()
            .map(AluguelMapper.INSTANCE::toDTO)
            .collect(Collectors.toList());

        // Para cada anúncio, calcula a média das avaliações
        for (AluguelDTO dto : dtos) {
            List<Avaliacao> avaliacoes = avaliacaoRepository
                .findByTipoItemAndItemId("aluguel", dto.getId());
            double media = 0.0;
            if (avaliacoes != null && !avaliacoes.isEmpty()) {
                media = avaliacoes.stream()
                    .mapToDouble(Avaliacao::getNota)
                    .average()
                    .orElse(0.0);
            }
            dto.setMediaAvaliacoes(media);
        }
        return dtos;
    }

    /**
     * Busca de anúncio de aluguel por ID
     */
    @Transactional(readOnly = true)
    public AluguelDTO buscarPorId(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));
        AluguelDTO dto = AluguelMapper.INSTANCE.toDTO(aluguel);
        List<Avaliacao> avaliacoes = avaliacaoRepository
            .findByTipoItemAndItemId("aluguel", dto.getId());
        double media = 0.0;
        if (avaliacoes != null && !avaliacoes.isEmpty()) {
            media = avaliacoes.stream()
                .mapToDouble(Avaliacao::getNota)
                .average()
                .orElse(0.0);
        }
        dto.setMediaAvaliacoes(media);
        return dto;
    }

    /**
     * Atualização de anúncio de aluguel
     */
    @Transactional
    public AluguelDTO atualizarAluguel(
        Long id,
        AluguelDTO aluguelDTO,
        MultipartFile[] novasFotos,
        String fotosExistentesJson
    ) {
        Aluguel aluguelExistente = aluguelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));

        Aluguel aluguelAtualizado = AluguelMapper.INSTANCE.toEntity(aluguelDTO);
        aluguelAtualizado.setId(aluguelExistente.getId());

        if (aluguelDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(aluguelDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            aluguelAtualizado.setUsuario(usuario);
        } else {
            aluguelAtualizado.setUsuario(aluguelExistente.getUsuario());
        }

        List<String> fotosExistentesFinal;
        try {
            if (fotosExistentesJson != null && !fotosExistentesJson.isEmpty()) {
                fotosExistentesFinal = objectMapper.readValue(
                    fotosExistentesJson,
                    new TypeReference<List<String>>() {}
                );
            } else {
                fotosExistentesFinal = new ArrayList<>();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar fotos existentes.", e);
        }

        List<Foto> fotosAtuais = aluguelExistente.getFotos() != null
            ? aluguelExistente.getFotos()
            : new ArrayList<>();
        List<Foto> fotosMantidas = fotosAtuais.stream()
            .filter(f -> fotosExistentesFinal.contains(f.getCaminho()))
            .collect(Collectors.toList());

        if (novasFotos != null) {
            for (MultipartFile file : novasFotos) {
                if (!file.isEmpty()) {
                    String caminhoSalvo = saveFile(file);
                    Foto novaFoto = new Foto();
                    novaFoto.setCaminho(caminhoSalvo);
                    novaFoto.setAluguel(aluguelAtualizado);
                    fotosMantidas.add(novaFoto);
                }
            }
        }

        aluguelAtualizado.setFotos(fotosMantidas);
        Aluguel salvo = aluguelRepository.save(aluguelAtualizado);
        AluguelDTO dto = AluguelMapper.INSTANCE.toDTO(salvo);

        // Atualiza a média das avaliações manualmente
        List<Avaliacao> avaliacoes = avaliacaoRepository
            .findByTipoItemAndItemId("aluguel", dto.getId());
        double media = 0.0;
        if (avaliacoes != null && !avaliacoes.isEmpty()) {
            media = avaliacoes.stream()
                .mapToDouble(Avaliacao::getNota)
                .average()
                .orElse(0.0);
        }
        dto.setMediaAvaliacoes(media);
        return dto;
    }

    /**
     * Deleção de anúncio de aluguel
     */
    public void deletarAluguel(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));
        aluguelRepository.delete(aluguel);
    }

    /**
     * Pesquisa de anúncios de aluguel
     */
    @Transactional(readOnly = true)
    public List<AluguelDTO> searchAlugueis(String query) {
        List<Aluguel> alugueis = aluguelRepository
            .findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(query, query);
        return alugueis.stream().map(aluguel -> {
            AluguelDTO dto = AluguelMapper.INSTANCE.toDTO(aluguel);
            List<Avaliacao> avaliacoes = avaliacaoRepository
                .findByTipoItemAndItemId("aluguel", dto.getId());
            double media = 0.0;
            if (avaliacoes != null && !avaliacoes.isEmpty()) {
                media = avaliacoes.stream()
                    .mapToDouble(Avaliacao::getNota)
                    .average()
                    .orElse(0.0);
            }
            dto.setMediaAvaliacoes(media);
            return dto;
        }).collect(Collectors.toList());
    }

    // === Métodos de moderação ===

    /**
     * Lista anúncios sinalizados para moderação
     */
    @Transactional(readOnly = true)
    public List<AluguelDTO> listarAnunciosSinalizados() {
        return aluguelRepository.findByFlaggedTrue().stream()
            .map(AluguelMapper.INSTANCE::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Remove (deleta) anúncio sinalizado
     */
    public void removeAnuncioSinalizado(Long id) {
        aluguelRepository.deleteById(id);
    }

    /**
     * Restaura anúncio sinalizado (limpa flag)
     */
    public void restaurarAnuncio(Long id) {
        Aluguel a = aluguelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
        a.setFlagged(false);
        a.setFlagReason(null);
        aluguelRepository.save(a);
    }

    /**
     * Salva arquivo no servidor
     */
    private String saveFile(MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return BASE_URL + "/" + UPLOAD_DIR + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo: " + e.getMessage(), e);
        }
    }
}
