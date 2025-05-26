package com.encontreaqui.service;

import com.encontreaqui.dto.ComercioDTO;
import com.encontreaqui.mapper.ComercioMapper;
import com.encontreaqui.model.Comercio;
import com.encontreaqui.model.Foto;
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
public class ComercioService {

    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String UPLOAD_DIR = "uploads";
    private static final String BASE_URL = "http://localhost:8080";

    /**
     * Cria um novo comércio.
     */
    public ComercioDTO criarComercio(ComercioDTO dto) {
        Comercio c = ComercioMapper.INSTANCE.toEntity(dto);
        if (dto.getUsuarioId() == null) {
            throw new RuntimeException("Campo usuarioId é obrigatório.");
        }
        c.setUsuario(
            usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."))
        );
        if (c.getFotos() != null) {
            c.getFotos().forEach(f -> f.setComercio(c));
        }
        c.setDataCriacao(new Date());
        c.setDataAtualizacao(new Date());
        Comercio salvo = comercioRepository.save(c);
        return ComercioMapper.INSTANCE.toDTO(salvo);
    }

    /**
     * Lista todos os comércios e define média de avaliações.
     */
    @Transactional(readOnly = true)
    public List<ComercioDTO> listarComercios() {
        return comercioRepository.findAll().stream()
            .map(c -> {
                ComercioDTO dto = ComercioMapper.INSTANCE.toDTO(c);
                Double avg = comercioRepository.findAverageRatingByCommerceId(c.getId());
                dto.setMediaAvaliacoes(avg != null ? avg : 0.0);
                return dto;
            })
            .collect(Collectors.toList());
    }

    /**
     * Busca comércio por ID.
     */
    @Transactional(readOnly = true)
    public ComercioDTO buscarPorId(Long id) {
        Comercio c = comercioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        ComercioDTO dto = ComercioMapper.INSTANCE.toDTO(c);
        Double avg = comercioRepository.findAverageRatingByCommerceId(id);
        dto.setMediaAvaliacoes(avg != null ? avg : 0.0);
        return dto;
    }

    /**
     * Atualiza um comércio existente.
     */
    @Transactional
    public ComercioDTO atualizarComercio(Long id, ComercioDTO dto, MultipartFile[] novasFotos, String fotosJson) {
        Comercio atual = comercioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        Comercio c = ComercioMapper.INSTANCE.toEntity(dto);
        c.setId(atual.getId());
        c.setDataCriacao(atual.getDataCriacao());
        c.setDataAtualizacao(new Date());
        if (dto.getUsuarioId() != null) {
            c.setUsuario(
                usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."))
            );
        } else {
            c.setUsuario(atual.getUsuario());
        }
        List<String> exist;
        try {
            exist = (fotosJson != null && !fotosJson.isEmpty())
                ? objectMapper.readValue(fotosJson, new TypeReference<List<String>>() {})
                : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar fotos existentes.", e);
        }
        List<Foto> mantidas = atual.getFotos().stream()
            .filter(f -> exist.contains(f.getCaminho()))
            .collect(Collectors.toList());
        if (novasFotos != null) {
            for (MultipartFile mf : novasFotos) {
                if (!mf.isEmpty()) {
                    String path = saveFile(mf);
                    Foto nf = new Foto(); nf.setCaminho(path); nf.setComercio(c);
                    mantidas.add(nf);
                }
            }
        }
        c.setFotos(mantidas);
        Comercio saved = comercioRepository.save(c);
        return ComercioMapper.INSTANCE.toDTO(saved);
    }

    /**
     * Deleta um comércio.
     */
    public void deletarComercio(Long id) {
        Comercio c = comercioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        comercioRepository.delete(c);
    }

    /**
     * Pesquisa comércios.
     */
    @Transactional(readOnly = true)
    public List<ComercioDTO> searchComercios(String q) {
        return comercioRepository
            .findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(q, q)
            .stream()
            .map(c -> {
                ComercioDTO dto = ComercioMapper.INSTANCE.toDTO(c);
                Double avg = comercioRepository.findAverageRatingByCommerceId(c.getId());
                dto.setMediaAvaliacoes(avg != null ? avg : 0.0);
                return dto;
            })
            .collect(Collectors.toList());
    }

    // === Moderação ===

    /**
     * Lista comércios sinalizados.
     */
    @Transactional(readOnly = true)
    public List<ComercioDTO> listarComerciosSinalizados() {
        return comercioRepository.findByFlaggedTrue()
            .stream()
            .map(ComercioMapper.INSTANCE::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Remove comércio sinalizado.
     */
    public void removeComercioSinalizado(Long id) {
        comercioRepository.deleteById(id);
    }

    /**
     * Restaura comércio sinalizado.
     */
    public void restaurarComercio(Long id) {
        Comercio c = comercioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        c.setFlagged(false);
        c.setFlagReason(null);
        comercioRepository.save(c);
    }

    /**
     * Salva arquivo e retorna URL.
     */
    private String saveFile(MultipartFile f) {
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();
            String name = System.currentTimeMillis() + "_" + f.getOriginalFilename();
            Path dest = Paths.get(UPLOAD_DIR, name);
            Files.copy(f.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
            return BASE_URL + "/" + UPLOAD_DIR + "/" + name;
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao salvar o arquivo: " + ex.getMessage(), ex);
        }
    }
}
