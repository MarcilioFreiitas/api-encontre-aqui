package com.encontreaqui.controller;

import com.encontreaqui.dto.AvaliacaoDTO;
import com.encontreaqui.dto.RespostaComentarioDTO;
import com.encontreaqui.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Avaliações", description = "Endpoints para avaliações e comentários")
@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Operation(
        summary = "Criar avaliação",
        description = "Cria uma avaliação para um item (comércio, serviço ou aluguel)."
    )
    @PostMapping
    public ResponseEntity<AvaliacaoDTO> criarAvaliacao(@Valid @RequestBody AvaliacaoDTO avaliacaoDTO) {
        AvaliacaoDTO novaAvaliacao = avaliacaoService.criarAvaliacao(avaliacaoDTO);
        return ResponseEntity.ok(novaAvaliacao);
    }

    @Operation(
        summary = "Buscar avaliação por ID",
        description = "Retorna os dados de uma avaliação específica, baseada no ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorId(id));
    }

    @Operation(
        summary = "Listar avaliações por item",
        description = "Retorna uma lista de avaliações para um item específico, dado seu tipo (ex: 'comercio') e ID."
    )
    @GetMapping("/item")
    public ResponseEntity<List<AvaliacaoDTO>> listarPorItem(
            @RequestParam("tipoItem") String tipoItem,
            @RequestParam("itemId") Long itemId) {
        List<AvaliacaoDTO> avaliacoes = avaliacaoService.listarPorItem(tipoItem, itemId);
        return ResponseEntity.ok(avaliacoes);
    }
    
    @Operation(
        summary = "Responder a uma avaliação",
        description = "Permite que o dono do anúncio responda ao comentário de uma avaliação."
    )
    @PostMapping("/responder")
    public ResponseEntity<AvaliacaoDTO> responderComentario(@Valid @RequestBody RespostaComentarioDTO dto) {
        AvaliacaoDTO atualizada = avaliacaoService.responderComentario(dto);
        return ResponseEntity.ok(atualizada);
    }
}
