package com.encontreaqui.service;

import com.encontreaqui.dto.ComercioDTO;
import com.encontreaqui.mapper.ComercioMapper;
import com.encontreaqui.model.Comercio;
import com.encontreaqui.model.Foto;
import com.encontreaqui.model.Usuario;
import com.encontreaqui.repository.ComercioRepository;
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
 * Service responsible for commerce operations including creation, listing,
 * updating (with photo management) and deletion.
 */
@Service
@Transactional
public class ComercioService {

    @Autowired
    private ComercioRepository comercioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository; // for associating the user

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Diretório fixo para armazenar as imagens – ajuste conforme necessário.
    // Neste exemplo, usamos "uploads" no mesmo diretório da aplicação.
    private static final String UPLOAD_DIR = "uploads";

    // Definição da BASE_URL para montar o caminho completo da imagem
    private static final String BASE_URL = "http://localhost:8080";

    // Creates a new commerce
    public ComercioDTO criarComercio(ComercioDTO comercioDTO) {
        Comercio comercio = ComercioMapper.INSTANCE.toEntity(comercioDTO);
        
        // Associate the user based on usuarioId provided in the DTO
        if (comercioDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(comercioDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            comercio.setUsuario(usuario);
        } else {
            throw new RuntimeException("Campo usuarioId é obrigatório.");
        }
        
        // Associate each photo with the commerce
        if (comercio.getFotos() != null) {
            comercio.getFotos().forEach(foto -> foto.setComercio(comercio));
        }
        
        comercio.setDataCriacao(new Date());
        comercio.setDataAtualizacao(new Date());
        Comercio salvo = comercioRepository.save(comercio);
        return ComercioMapper.INSTANCE.toDTO(salvo);
    }

    // Lists all commerces
    @Transactional(readOnly = true)
    public List<ComercioDTO> listarComercios() {
        return comercioRepository.findAll().stream()
                .map(ComercioMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    // Finds a commerce by its ID
    @Transactional(readOnly = true)
    public ComercioDTO buscarPorId(Long id) {
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        return ComercioMapper.INSTANCE.toDTO(comercio);
    }

    /**
     * Updates an existing commerce, allowing editing of fields and management
     * of photos (removing existing and adding new ones).
     *
     * @param id                   ID of the commerce to update.
     * @param comercioDTO          Updated commerce data (except photos).
     * @param novasFotos           Array of new photo files (optional).
     * @param fotosExistentesJson  JSON string representing the list of existing photo paths to keep (optional).
     * @return The updated commerce data.
     */
    @Transactional
    public ComercioDTO atualizarComercio(Long id, ComercioDTO comercioDTO, MultipartFile[] novasFotos, String fotosExistentesJson) {
        Comercio comercioExistente = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        
        // Convert the DTO into an entity to capture updated fields
        Comercio comercioAtualizado = ComercioMapper.INSTANCE.toEntity(comercioDTO);
        comercioAtualizado.setId(comercioExistente.getId());
        comercioAtualizado.setDataCriacao(comercioExistente.getDataCriacao());
        comercioAtualizado.setDataAtualizacao(new Date());
        
        // Associate the user: if provided in DTO, fetch; otherwise, keep the existing user
        if (comercioDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(comercioDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            comercioAtualizado.setUsuario(usuario);
        } else {
            comercioAtualizado.setUsuario(comercioExistente.getUsuario());
        }
        
        // Process existing photos (parse the JSON to get paths to keep)
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
        
        // Get current photos and filter those that should be kept
        List<Foto> fotosAtuais = comercioExistente.getFotos() != null ? comercioExistente.getFotos() : new ArrayList<>();
        List<Foto> fotosMantidas = fotosAtuais.stream()
                .filter(f -> fotosExistentesFinal.contains(f.getCaminho()))
                .collect(Collectors.toList());
        
        // Process new photos, if present
        if (novasFotos != null) {
            for (MultipartFile file : novasFotos) {
                if (!file.isEmpty()) {
                    // Save the file using the storage logic in the specified upload directory
                    String caminhoSalvo = saveFile(file);
                    Foto novaFoto = new Foto();
                    novaFoto.setCaminho(caminhoSalvo);
                    novaFoto.setComercio(comercioAtualizado);
                    fotosMantidas.add(novaFoto);
                }
            }
        }
        
        // Associate the collected photos (kept and new) with the commerce
        comercioAtualizado.setFotos(fotosMantidas);
        
        Comercio salvo = comercioRepository.save(comercioAtualizado);
        return ComercioMapper.INSTANCE.toDTO(salvo);
    }
    
    /**
     * Saves the given MultipartFile into storage and returns the stored file's path.
     * This implementation stores the file in a fixed directory defined by UPLOAD_DIR.
     * Make sure the directory exists or is created.
     *
     * @param file the file to save.
     * @return the complete URL of the stored file, e.g., "http://localhost:8080/uploads/..."
     */
    private String saveFile(MultipartFile file) {
        try {
            // Ensure the upload directory exists
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Generate a unique filename using the current timestamp and the original filename
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            
            // Get the full path for saving the file
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            
            // Copy the file contents to the destination, replacing if it already exists
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Return the complete URL including the BASE_URL. Use "/" as separator to compose the URL.
            return BASE_URL + "/" + UPLOAD_DIR + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo: " + e.getMessage(), e);
        }
    }

    // Deletes a commerce
    public void deletarComercio(Long id) {
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        comercioRepository.delete(comercio);
    }
}
