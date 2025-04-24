package com.encontreaqui.service;

import com.encontreaqui.dto.AluguelDTO;
import com.encontreaqui.mapper.AluguelMapper;
import com.encontreaqui.model.Aluguel;
import com.encontreaqui.repository.AluguelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional  // Todos os métodos serão, por padrão, transacionais de escrita
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    // Método de escrita: criação
    public AluguelDTO criarAluguel(AluguelDTO aluguelDTO) {
        Aluguel aluguel = AluguelMapper.INSTANCE.toEntity(aluguelDTO);
        Aluguel salvo = aluguelRepository.save(aluguel);
        return AluguelMapper.INSTANCE.toDTO(salvo);
    }

    // Método somente de leitura: lista os anúncios de aluguel
    @Transactional(readOnly = true)
    public List<AluguelDTO> listarAlugueis() {
        return aluguelRepository.findAll().stream()
                .map(AluguelMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    // Método somente de leitura: busca anúncio de aluguel pelo ID
    @Transactional(readOnly = true)
    public AluguelDTO buscarPorId(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));
        return AluguelMapper.INSTANCE.toDTO(aluguel);
    }

    // Método de escrita: atualização
    public AluguelDTO atualizarAluguel(Long id, AluguelDTO aluguelDTO) {
        Aluguel aluguelExistente = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));
        Aluguel aluguelAtualizado = AluguelMapper.INSTANCE.toEntity(aluguelDTO);
        aluguelAtualizado.setId(aluguelExistente.getId()); // Mantém o ID
        Aluguel salvo = aluguelRepository.save(aluguelAtualizado);
        return AluguelMapper.INSTANCE.toDTO(salvo);
    }

    // Método de escrita: deleção
    public void deletarAluguel(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));
        aluguelRepository.delete(aluguel);
    }
}
