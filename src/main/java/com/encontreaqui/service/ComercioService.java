package com.encontreaqui.service;

import com.encontreaqui.dto.ComercioDTO;
import com.encontreaqui.mapper.ComercioMapper;
import com.encontreaqui.model.Comercio;
import com.encontreaqui.model.Foto;
import com.encontreaqui.repository.ComercioRepository;
import com.encontreaqui.repository.UsuarioRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service responsável pelas operações relacionadas ao comércio (criação, listagem, 
 * atualização com gerenciamento de fotos e deleção).
 */
@Service
@Transactional
public class ComercioService {

    @Autowired
    private ComercioRepository comercioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository; // para associar o usuário

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Diretório fixo onde as imagens serão armazenadas – ajuste conforme necessário.
    private static final String UPLOAD_DIR = "uploads";
    
    // BASE_URL para montagem do caminho completo da imagem
    private static final String BASE_URL = "http://localhost:8080";

    /**
     * Cria um novo comércio.
     */
    public ComercioDTO criarComercio(ComercioDTO comercioDTO) {
        Comercio comercio = ComercioMapper.INSTANCE.toEntity(comercioDTO);
        
        if (comercioDTO.getUsuarioId() != null) {
            comercio.setUsuario(
                usuarioRepository.findById(comercioDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."))
            );
        } else {
            throw new RuntimeException("Campo usuarioId é obrigatório.");
        }
        
        if (comercio.getFotos() != null) {
            comercio.getFotos().forEach(foto -> foto.setComercio(comercio));
        }
        
        comercio.setDataCriacao(new Date());
        comercio.setDataAtualizacao(new Date());
        
        Comercio salvo = comercioRepository.save(comercio);
        return ComercioMapper.INSTANCE.toDTO(salvo);
    }

    /**
     * Lista todos os comércios e, para cada comércio, calcula a média das avaliações.
     * Nota: A consulta de média é feita via query definida no repositório (findAverageRatingByCommerceId).
     */
    @Transactional(readOnly = true)
    public List<ComercioDTO> listarComercios() {
        return comercioRepository.findAll().stream()
            .map(comercio -> {
                ComercioDTO dto = ComercioMapper.INSTANCE.toDTO(comercio);
                Double media = comercioRepository.findAverageRatingByCommerceId(comercio.getId());
                dto.setMediaAvaliacoes(media != null ? media : 0.0);
                return dto;
            })
            .collect(Collectors.toList());
    }

    /**
     * Busca um comércio pelo ID e calcula a média de suas avaliações.
     */
    @Transactional(readOnly = true)
    public ComercioDTO buscarPorId(Long id) {
        Comercio comercio = comercioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        
        Double media = comercioRepository.findAverageRatingByCommerceId(comercio.getId());
        ComercioDTO dto = ComercioMapper.INSTANCE.toDTO(comercio);
        dto.setMediaAvaliacoes(media != null ? media : 0.0);
        return dto;
    }

    /**
     * Atualiza um comércio existente, permitindo a edição de campos e a gestão de fotos 
     * (remoção de fotos existentes e adição de novas).
     */
    @Transactional
    public ComercioDTO atualizarComercio(Long id, ComercioDTO comercioDTO, MultipartFile[] novasFotos, String fotosExistentesJson) {
        Comercio comercioExistente = comercioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        
        // Converte o DTO para a entidade; preserva o ID e a data de criação.
        Comercio comercioAtualizado = ComercioMapper.INSTANCE.toEntity(comercioDTO);
        comercioAtualizado.setId(comercioExistente.getId());
        comercioAtualizado.setDataCriacao(comercioExistente.getDataCriacao());
        comercioAtualizado.setDataAtualizacao(new Date());
        
        if (comercioDTO.getUsuarioId() != null) {
            comercioAtualizado.setUsuario(
                usuarioRepository.findById(comercioDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."))
            );
        } else {
            comercioAtualizado.setUsuario(comercioExistente.getUsuario());
        }
        
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
        
        List<Foto> fotosAtuais = comercioExistente.getFotos() != null ? comercioExistente.getFotos() : new ArrayList<>();
        List<Foto> fotosMantidas = fotosAtuais.stream()
            .filter(f -> fotosExistentesFinal.contains(f.getCaminho()))
            .collect(Collectors.toList());
        
        if (novasFotos != null) {
            for (MultipartFile file : novasFotos) {
                if (!file.isEmpty()) {
                    String caminhoSalvo = saveFile(file);
                    Foto novaFoto = new Foto();
                    novaFoto.setCaminho(caminhoSalvo);
                    novaFoto.setComercio(comercioAtualizado);
                    fotosMantidas.add(novaFoto);
                }
            }
        }
        
        comercioAtualizado.setFotos(fotosMantidas);
        Comercio salvo = comercioRepository.save(comercioAtualizado);
        return ComercioMapper.INSTANCE.toDTO(salvo);
    }
    
    /**
     * Método auxiliar para salvar o arquivo enviado via MultipartFile.
     * Retorna o caminho completo (URL) do arquivo armazenado.
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

    /**
     * Deleta um comércio pelo ID.
     */
    public void deletarComercio(Long id) {
        Comercio comercio = comercioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        comercioRepository.delete(comercio);
    }
    
    /**
     * NOVA FUNCIONALIDADE DE PESQUISA:
     * Busca comércios cujo título ou categoria contenha o termo de pesquisa (ignorando case)
     * e, para cada comércio encontrado, calcula a média das avaliações.
     */
    @Transactional(readOnly = true)
    public List<ComercioDTO> searchComercios(String query) {
        // Usa o método customizado para buscar por título ou categoria
        List<Comercio> comercios = comercioRepository
            .findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(query, query);
        
        return comercios.stream().map(comercio -> {
            ComercioDTO dto = ComercioMapper.INSTANCE.toDTO(comercio);
            Double media = comercioRepository.findAverageRatingByCommerceId(comercio.getId());
            dto.setMediaAvaliacoes(media != null ? media : 0.0);
            return dto;
        }).collect(Collectors.toList());
    }
}
