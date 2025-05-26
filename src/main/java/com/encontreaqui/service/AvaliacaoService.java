package com.encontreaqui.service;

import com.encontreaqui.dto.AvaliacaoDTO;
import com.encontreaqui.dto.RespostaComentarioDTO;
import com.encontreaqui.mapper.AvaliacaoMapper;
import com.encontreaqui.model.Avaliacao;
import com.encontreaqui.model.Usuario;
import com.encontreaqui.repository.AluguelRepository;
import com.encontreaqui.repository.AvaliacaoRepository;
import com.encontreaqui.repository.ComercioRepository;
import com.encontreaqui.repository.ServicoRepository;
import com.encontreaqui.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ComercioRepository comercioRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private AluguelRepository aluguelRepository;
    
    @Autowired
    private AutenticacaoService autenticacaoService;

    // Cria uma nova avaliação
    public AvaliacaoDTO criarAvaliacao(AvaliacaoDTO avaliacaoDTO) {
        // Recupera o usuário pelo id; se não existir, lança exceção.
        Usuario usuario = usuarioRepository.findById(avaliacaoDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        
        Avaliacao avaliacao = AvaliacaoMapper.INSTANCE.toEntity(avaliacaoDTO);
        avaliacao.setUsuario(usuario);
        // A data de cadastro já é definida no construtor / @PrePersist da entidade.
        
        Avaliacao salva = avaliacaoRepository.save(avaliacao);
        return AvaliacaoMapper.INSTANCE.toDTO(salva);
    }
    
    // Busca uma avaliação por ID
    @Transactional(readOnly = true)
    public AvaliacaoDTO buscarPorId(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));
        return AvaliacaoMapper.INSTANCE.toDTO(avaliacao);
    }
    
    // Lista todas as avaliações para um item específico (por tipo e id)
    @Transactional(readOnly = true)
    public List<AvaliacaoDTO> listarPorItem(String tipoItem, Long itemId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByTipoItemAndItemId(tipoItem, itemId);
        return avaliacoes.stream()
                .map(AvaliacaoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }
    
    // Permite responder a um comentário, somente se o usuário logado for o dono do anúncio
    @Transactional
    public AvaliacaoDTO responderComentario(RespostaComentarioDTO dto) {
        // Busca o comentário original
        Avaliacao avaliacaoOriginal = avaliacaoRepository.findById(dto.getComentarioId())
            .orElseThrow(() -> new RuntimeException("Comentário não encontrado."));
        
        // Obter o ID do usuário logado via JWT, utilizando o AutenticacaoService
        Long usuarioLogadoId = autenticacaoService.obterUsuarioLogadoId();
        
        // Verifica se o usuário logado é o dono do anúncio relacionado ao comentário
        String tipoItem = avaliacaoOriginal.getTipoItem();
        Long itemId = avaliacaoOriginal.getItemId();
        Long donoDoAnuncioId = buscarDonoDoAnuncio(tipoItem, itemId);
        
        if (!usuarioLogadoId.equals(donoDoAnuncioId)) {
            throw new RuntimeException("Você não pode responder a esse comentário.");
        }
        
        // Atualiza o campo resposta do comentário
        avaliacaoOriginal.setResposta(dto.getResposta());
        
        // Salva a atualização
        Avaliacao updated = avaliacaoRepository.save(avaliacaoOriginal);
        
        return AvaliacaoMapper.INSTANCE.toDTO(updated);
    }
    
    // Método auxiliar para buscar o dono do anúncio a partir do tipo e do ID do item
    public Long buscarDonoDoAnuncio(String tipoItem, Long itemId) {
        if ("comercio".equalsIgnoreCase(tipoItem)) {
            return comercioRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Comércio não encontrado."))
                .getUsuario().getId();
        } else if ("servico".equalsIgnoreCase(tipoItem)) {
            return servicoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado."))
                .getUsuario().getId();
        } else if ("aluguel".equalsIgnoreCase(tipoItem)) {
            return aluguelRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado."))
                .getUsuario().getId();
        } else {
            throw new RuntimeException("Tipo de item inválido.");
        }
    }
    
    // Outros métodos (atualizar, excluir etc.) podem ser adicionados conforme necessário
}
