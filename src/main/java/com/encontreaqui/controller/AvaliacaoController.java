package com.encontreaqui.controller;

import com.encontreaqui.dto.AvaliacaoDTO;
import com.encontreaqui.dto.RespostaComentarioDTO;
import com.encontreaqui.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Avaliações", description = "Endpoints para avaliações e comentários")
@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @Operation(
        summary = "Criar avaliação",
        description = "Cria uma avaliação para um item (comércio, serviço ou aluguel)."
    )
    @ApiResponse(responseCode = "200", description = "Avaliação criada com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvaliacaoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos da avaliação", content = @Content)
    @PostMapping
    public ResponseEntity<AvaliacaoDTO> criarAvaliacao(
        @Parameter(description = "Dados da avaliação a ser criada", required = true)
        @Valid @RequestBody AvaliacaoDTO avaliacaoDTO
    ) {
        AvaliacaoDTO dto = avaliacaoService.criarAvaliacao(avaliacaoDTO);
        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "Buscar avaliação por ID",
        description = "Retorna os dados de uma avaliação específica, baseada no ID informado."
    )
    @ApiResponse(responseCode = "200", description = "Avaliação encontrada com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvaliacaoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> buscarPorId(
        @Parameter(description = "ID da avaliação", required = true)
        @PathVariable Long id
    ) {
        AvaliacaoDTO dto = avaliacaoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "Listar avaliações por item",
        description = "Retorna uma lista de avaliações para um item específico, dado seu tipo (ex: 'comercio') e ID do item."
    )
    @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvaliacaoDTO.class)))
    @GetMapping("/item")
    public ResponseEntity<List<AvaliacaoDTO>> listarPorItem(
        @Parameter(description = "Tipo do item avaliado (comercio, servico ou aluguel)", required = true)
        @RequestParam("tipoItem") String tipoItem,
        @Parameter(description = "ID do item avaliado", required = true)
        @RequestParam("itemId") Long itemId
    ) {
        List<AvaliacaoDTO> lista = avaliacaoService.listarPorItem(tipoItem, itemId);
        return ResponseEntity.ok(lista);
    }

    @Operation(
        summary = "Responder a uma avaliação",
        description = "Permite que o dono do anúncio responda ao comentário de uma avaliação."
    )
    @ApiResponse(responseCode = "200", description = "Resposta adicionada com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvaliacaoDTO.class)))
    @ApiResponse(responseCode = "403", description = "Usuário não autorizado a responder", content = @Content)
    @PostMapping("/responder")
    public ResponseEntity<AvaliacaoDTO> responderComentario(
        @Parameter(description = "Dados da resposta ao comentário", required = true)
        @Valid @RequestBody RespostaComentarioDTO dto
    ) {
        AvaliacaoDTO updated = avaliacaoService.responderComentario(dto);
        return ResponseEntity.ok(updated);
    }
}
