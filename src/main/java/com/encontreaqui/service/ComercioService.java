package com.encontreaqui.service;

import com.encontreaqui.dto.ComercioDTO;
import com.encontreaqui.mapper.ComercioMapper;
import com.encontreaqui.model.Comercio;
import com.encontreaqui.repository.ComercioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional  // Define que todos os métodos, por padrão, serão transacionais de escrita
public class ComercioService {

    @Autowired
    private ComercioRepository comercioRepository;

    // Método de escrita: criação
    public ComercioDTO criarComercio(ComercioDTO comercioDTO) {
        Comercio comercio = ComercioMapper.INSTANCE.toEntity(comercioDTO);
        Comercio salvo = comercioRepository.save(comercio);
        return ComercioMapper.INSTANCE.toDTO(salvo);
    }

    // Método somente de leitura: lista os comércios
    @Transactional(readOnly = true)
    public List<ComercioDTO> listarComercios() {
        return comercioRepository.findAll().stream()
                .map(ComercioMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    // Método somente de leitura: busca comércio por ID
    @Transactional(readOnly = true)
    public ComercioDTO buscarPorId(Long id) {
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        return ComercioMapper.INSTANCE.toDTO(comercio);
    }

    // Método de escrita: atualização
    public ComercioDTO atualizarComercio(Long id, ComercioDTO comercioDTO) {
        Comercio comercioExistente = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        Comercio comercioAtualizado = ComercioMapper.INSTANCE.toEntity(comercioDTO);
        comercioAtualizado.setId(comercioExistente.getId()); // Mantém o ID
        Comercio salvo = comercioRepository.save(comercioAtualizado);
        return ComercioMapper.INSTANCE.toDTO(salvo);
    }

    // Método de escrita: deleção
    public void deletarComercio(Long id) {
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comércio não encontrado."));
        comercioRepository.delete(comercio);
    }
}
