package com.encontreaqui.service;

import com.encontreaqui.dto.ServicoDTO;
import com.encontreaqui.mapper.ServicoMapper;
import com.encontreaqui.model.Servico;
import com.encontreaqui.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    // Cria um novo Serviço convertendo o DTO em entidade, salvando e retornando o DTO
    public ServicoDTO criarServico(ServicoDTO servicoDTO) {
        Servico servico = ServicoMapper.INSTANCE.toEntity(servicoDTO);
        Servico salvo = servicoRepository.save(servico);
        return ServicoMapper.INSTANCE.toDTO(salvo);
    }

    // Lista todos os Serviços e os converte para DTO
    public List<ServicoDTO> listarServicos() {
        List<Servico> servicos = servicoRepository.findAll();
        return servicos.stream()
                .map(ServicoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    // Busca um Serviço por ID e converte para DTO, ou lança exceção se não encontrado
    public ServicoDTO buscarPorId(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        return ServicoMapper.INSTANCE.toDTO(servico);
    }

    // Atualiza um Serviço: busca o serviço existente, converte os dados do DTO para entidade, preserva o ID e salva
    public ServicoDTO atualizarServico(Long id, ServicoDTO servicoDTO) {
        Servico servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        Servico servicoAtualizado = ServicoMapper.INSTANCE.toEntity(servicoDTO);
        servicoAtualizado.setId(servicoExistente.getId()); // Preserva o ID
        Servico salvo = servicoRepository.save(servicoAtualizado);
        return ServicoMapper.INSTANCE.toDTO(salvo);
    }

    // Deleta um Serviço após verificar se existe
    public void deletarServico(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        servicoRepository.delete(servico);
    }
}
