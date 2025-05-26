package com.encontreaqui.service;

import com.encontreaqui.dto.ServicoDTO;
import com.encontreaqui.mapper.ServicoMapper;
import com.encontreaqui.model.Foto;
import com.encontreaqui.model.Servico;
import com.encontreaqui.model.Usuario;
import com.encontreaqui.repository.AvaliacaoRepository;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final ObjectMapper objectMapper;

    private static final String UPLOAD_DIR = "uploads";
    private static final String BASE_URL = "http://localhost:8080";

    @Autowired
    public ServicoService(ServicoRepository servicoRepository,
                          UsuarioRepository usuarioRepository,
                          AvaliacaoRepository avaliacaoRepository) {
        this.servicoRepository = servicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.objectMapper = new ObjectMapper();
    }

    public ServicoDTO criarServico(ServicoDTO dto) {
        Servico servico = ServicoMapper.INSTANCE.toEntity(dto);
        associateUsuario(servico, dto.getUsuarioId());
        servico.setDataCriacao(new Date());
        servico.setDataAtualizacao(new Date());
        servico.setFlagged(false);
        servico.setFlagReason(null);
        servico.setFotos(initFotos(servico, dto.getFotos()));

        Servico salvo = servicoRepository.save(servico);
        return toDtoWithRating(salvo);
    }

    @Transactional(readOnly = true)
    public List<ServicoDTO> listarServicos() {
        return servicoRepository.findAll().stream()
                .map(this::toDtoWithRating)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicoDTO buscarPorId(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado id=" + id));
        return toDtoWithRating(servico);
    }

    public ServicoDTO atualizarServico(Long id, ServicoDTO dto,
                                       MultipartFile[] novasFotos,
                                       String fotosExistentesJson) {
        Servico existente = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado id=" + id));
        Servico upd = ServicoMapper.INSTANCE.toEntity(dto);
        upd.setId(id);
        upd.setDataCriacao(existente.getDataCriacao());
        upd.setDataAtualizacao(new Date());
        upd.setFlagged(existente.isFlagged());
        upd.setFlagReason(existente.getFlagReason());
        associateUsuario(upd, dto.getUsuarioId());

        List<String> kept = parseExistingPhotos(fotosExistentesJson);
        List<Foto> fotos = mergeFotos(existente.getFotos(), kept, novasFotos, upd);
        upd.setFotos(fotos);

        Servico saved = servicoRepository.save(upd);
        return toDtoWithRating(saved);
    }

    public void deletarServico(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado id=" + id));
        servicoRepository.delete(servico);
    }

    @Transactional(readOnly = true)
    public List<ServicoDTO> searchServicos(String query) {
        return servicoRepository
                .findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(query, query)
                .stream()
                .map(this::toDtoWithRating)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicoDTO> listarServicosFlagged() {
        return servicoRepository.findByFlaggedTrue().stream()
                .map(this::toDtoWithRating)
                .collect(Collectors.toList());
    }

    public void flagServico(Long id, String reason) {
        Servico s = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado id=" + id));
        s.setFlagged(true);
        s.setFlagReason(reason);
        servicoRepository.save(s);
    }

    public void unflagServico(Long id) {
        Servico s = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado id=" + id));
        s.setFlagged(false);
        s.setFlagReason(null);
        servicoRepository.save(s);
    }

    // --- helpers ---
    private ServicoDTO toDtoWithRating(Servico servico) {
        ServicoDTO dto = ServicoMapper.INSTANCE.toDTO(servico);
        double media = avaliacaoRepository
                .findByTipoItemAndItemId("servico", servico.getId())
                .stream().mapToDouble(a -> a.getNota()).average().orElse(0.0);
        dto.setMediaAvaliacoes(media);
        return dto;
    }

    private void associateUsuario(Servico servico, Long usuarioId) {
        if (usuarioId == null) {
            throw new RuntimeException("Campo usuarioId é obrigatório.");
        }
        Usuario u = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado id=" + usuarioId));
        servico.setUsuario(u);
    }

    private List<String> parseExistingPhotos(String json) {
        if (json == null || json.isEmpty()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler fotos existentes.", e);
        }
    }

    private List<Foto> initFotos(Servico servico, List<String> paths) {
        if (paths == null) return List.of();
        return paths.stream().map(path -> {
            Foto f = new Foto(); f.setCaminho(path); f.setServico(servico); return f;
        }).collect(Collectors.toList());
    }

    private List<Foto> mergeFotos(List<Foto> atuais, List<String> keep,
                                  MultipartFile[] novas, Servico servico) {
        List<Foto> mantidas = atuais.stream()
                .filter(f -> keep.contains(f.getCaminho()))
                .collect(Collectors.toList());
        if (novas != null) {
            for (MultipartFile file : novas) {
                if (!file.isEmpty()) {
                    String url = saveFile(file);
                    Foto f = new Foto(); f.setCaminho(url); f.setServico(servico);
                    mantidas.add(f);
                }
            }
        }
        return mantidas;
    }

    private String saveFile(MultipartFile file) {
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();
            String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path target = Paths.get(UPLOAD_DIR, name);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return BASE_URL + "/" + UPLOAD_DIR + "/" + name;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo.", e);
        }
    }
}
