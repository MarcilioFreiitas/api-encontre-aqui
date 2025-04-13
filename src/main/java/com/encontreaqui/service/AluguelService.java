package com.encontreaqui.service;

import com.encontreaqui.dto.AluguelDTO;
import com.encontreaqui.mapper.AluguelMapper;
import com.encontreaqui.model.Aluguel;
import com.encontreaqui.repository.AluguelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    public AluguelDTO criarAluguel(AluguelDTO aluguelDTO) {
        Aluguel aluguel = AluguelMapper.INSTANCE.toEntity(aluguelDTO);
        Aluguel salvo = aluguelRepository.save(aluguel);
        return AluguelMapper.INSTANCE.toDTO(salvo);
    }

    public List<AluguelDTO> listarAlugueis() {
        return aluguelRepository.findAll().stream()
                .map(AluguelMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public AluguelDTO buscarPorId(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));
        return AluguelMapper.INSTANCE.toDTO(aluguel);
    }

    public AluguelDTO atualizarAluguel(Long id, AluguelDTO aluguelDTO) {
        Aluguel aluguelExistente = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));
        Aluguel aluguelAtualizado = AluguelMapper.INSTANCE.toEntity(aluguelDTO);
        aluguelAtualizado.setId(aluguelExistente.getId()); // Mantém o ID
        Aluguel salvo = aluguelRepository.save(aluguelAtualizado);
        return AluguelMapper.INSTANCE.toDTO(salvo);
    }

    public void deletarAluguel(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."));
        aluguelRepository.delete(aluguel);
    }
}
