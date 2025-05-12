package com.encontreaqui.service;

import com.encontreaqui.dto.ServicoDTO;
import com.encontreaqui.mapper.ServicoMapper;
import com.encontreaqui.model.Foto;
import com.encontreaqui.model.Servico;
import com.encontreaqui.model.Usuario;
import com.encontreaqui.model.Avaliacao;
import com.encontreaqui.repository.ServicoRepository;
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
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AvaliacaoRepository avaliacaoRepository; // Para buscar as avaliações e calcular a média

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String UPLOAD_DIR = "uploads";
    private static final String BASE_URL = "http://localhost:8080";

    // Metodo para criar um novo serviço
    public ServicoDTO criarServico(ServicoDTO servicoDTO) {
        Servico servico = ServicoMapper.INSTANCE.toEntity(servicoDTO);
        if (servicoDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(servicoDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            servico.setUsuario(usuario);
        } else {
            throw new RuntimeException("Campo usuarioId é obrigatório.");
        }
        if (servico.getFotos() != null) {
            servico.getFotos().forEach(foto -> foto.setServico(servico));
        }
        servico.setDataCriacao(new Date());
        servico.setDataAtualizacao(new Date());
        Servico salvo = servicoRepository.save(servico);
        ServicoDTO dto = ServicoMapper.INSTANCE.toDTO(salvo);
        dto.setMediaAvaliacoes(0.0);
        return dto;
    }

    // Método para listar todos os serviços com cálculo da média das avaliações
    @Transactional(readOnly = true)
    public List<ServicoDTO> listarServicos() {
        List<Servico> servicos = servicoRepository.findAll();
        List<ServicoDTO> dtos = servicos.stream()
                .map(ServicoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
        for (ServicoDTO dto : dtos) {
            List<Avaliacao> avaliacoes = avaliacaoRepository.findByTipoItemAndItemId("servico", dto.getId());
            Double media = 0.0;
            if (avaliacoes != null && !avaliacoes.isEmpty()) {
                media = avaliacoes.stream().mapToDouble(a -> a.getNota()).average().orElse(0.0);
            }
            dto.setMediaAvaliacoes(media);
        }
        return dtos;
    }

    // Método para buscar um serviço por ID e calcular a média das avaliações
    @Transactional(readOnly = true)
    public ServicoDTO buscarPorId(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        ServicoDTO dto = ServicoMapper.INSTANCE.toDTO(servico);
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByTipoItemAndItemId("servico", dto.getId());
        Double media = 0.0;
        if (avaliacoes != null && !avaliacoes.isEmpty()) {
            media = avaliacoes.stream().mapToDouble(a -> a.getNota()).average().orElse(0.0);
        }
        dto.setMediaAvaliacoes(media);
        return dto;
    }

    // Método para atualizar um serviço, gerenciando também as imagens
    @Transactional
    public ServicoDTO atualizarServico(Long id, ServicoDTO servicoDTO, MultipartFile[] novasFotos, String fotosExistentesJson) {
        Servico servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        Servico servicoAtualizado = ServicoMapper.INSTANCE.toEntity(servicoDTO);
        servicoAtualizado.setId(servicoExistente.getId());
        servicoAtualizado.setDataCriacao(servicoExistente.getDataCriacao());
        servicoAtualizado.setDataAtualizacao(new Date());
        
        if (servicoDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(servicoDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
            servicoAtualizado.setUsuario(usuario);
        } else {
            servicoAtualizado.setUsuario(servicoExistente.getUsuario());
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
        
        List<Foto> fotosAtuais = servicoExistente.getFotos() != null ? servicoExistente.getFotos() : new ArrayList<>();
        List<Foto> fotosMantidas = fotosAtuais.stream()
                .filter(f -> fotosExistentesFinal.contains(f.getCaminho()))
                .collect(Collectors.toList());
        
        if (novasFotos != null) {
            for (MultipartFile file : novasFotos) {
                if (!file.isEmpty()) {
                    String caminhoSalvo = saveFile(file);
                    Foto novaFoto = new Foto();
                    novaFoto.setCaminho(caminhoSalvo);
                    novaFoto.setServico(servicoAtualizado);
                    fotosMantidas.add(novaFoto);
                }
            }
        }
        
        servicoAtualizado.setFotos(fotosMantidas);
        
        Servico salvo = servicoRepository.save(servicoAtualizado);
        ServicoDTO dto = ServicoMapper.INSTANCE.toDTO(salvo);
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByTipoItemAndItemId("servico", dto.getId());
        Double media = 0.0;
        if (avaliacoes != null && !avaliacoes.isEmpty()) {
            media = avaliacoes.stream().mapToDouble(a -> a.getNota()).average().orElse(0.0);
        }
        dto.setMediaAvaliacoes(media);
        return dto;
    }

    // Método auxiliar para salvar arquivos e retornar a URL completa
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
    
    // Método de pesquisa: busca serviços cujo título ou categoria contenha o termo (ignora case)
    @Transactional(readOnly = true)
    public List<ServicoDTO> searchServicos(String query) {
        // Usa o novo método do repositório
        List<Servico> servicos = servicoRepository.findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(query, query);
        List<ServicoDTO> dtos = servicos.stream()
                .map(ServicoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
        for (ServicoDTO dto : dtos) {
            List<Avaliacao> avaliacoes = avaliacaoRepository.findByTipoItemAndItemId("servico", dto.getId());
            Double media = 0.0;
            if (avaliacoes != null && !avaliacoes.isEmpty()) {
                media = avaliacoes.stream().mapToDouble(a -> a.getNota()).average().orElse(0.0);
            }
            dto.setMediaAvaliacoes(media);
        }
        return dtos;
    }
    
    public void deletarServico(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        servicoRepository.delete(servico);
    }
}
