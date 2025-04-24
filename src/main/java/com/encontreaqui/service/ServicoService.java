package com.encontreaqui.service;

import com.encontreaqui.dto.ServicoDTO;
import com.encontreaqui.mapper.ServicoMapper;
import com.encontreaqui.model.Servico;
import com.encontreaqui.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional // Todos os métodos serão transacionais, por padrão de escrita
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    // Método de escrita: criação. Em caso de exceção, a transação reverte tudo.
    public ServicoDTO criarServico(ServicoDTO servicoDTO) {
        Servico servico = ServicoMapper.INSTANCE.toEntity(servicoDTO);
        Servico salvo = servicoRepository.save(servico);
        return ServicoMapper.INSTANCE.toDTO(salvo);
    }

    // Método somente de leitura: utiliza readOnly para otimização
    @Transactional(readOnly = true)
    public List<ServicoDTO> listarServicos() {
        List<Servico> servicos = servicoRepository.findAll();
        return servicos.stream()
                .map(ServicoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicoDTO buscarPorId(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        return ServicoMapper.INSTANCE.toDTO(servico);
    }

    // Método de escrita: atualização
    public ServicoDTO atualizarServico(Long id, ServicoDTO servicoDTO) {
        Servico servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        Servico servicoAtualizado = ServicoMapper.INSTANCE.toEntity(servicoDTO);
        servicoAtualizado.setId(servicoExistente.getId()); // Preserva o ID
        Servico salvo = servicoRepository.save(servicoAtualizado);
        return ServicoMapper.INSTANCE.toDTO(salvo);
    }

    // Método de escrita: deleção
    public void deletarServico(Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado para o id: " + id));
        servicoRepository.delete(servico);
    }
}

