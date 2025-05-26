package com.encontreaqui.controller;

import com.encontreaqui.dto.AluguelDTO;
import com.encontreaqui.service.AluguelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Aluguel", description = "Endpoints para gerenciamento de anúncios de aluguel")
@RestController
@RequestMapping("/api/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    @Operation(
        summary = "Pesquisar anúncios de aluguel",
        description = "Retorna os anúncios de aluguel cujo título ou categoria contenha o termo de pesquisa (ignorando case)."
    )
    @GetMapping("/search")
    public ResponseEntity<List<AluguelDTO>> search(@RequestParam("q") String query) {
        List<AluguelDTO> result = aluguelService.searchAlugueis(query);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Listagem de anúncios de aluguel",
        description = "Retorna uma lista de todos os anúncios de aluguel cadastrados."
    )
    @GetMapping
    public ResponseEntity<List<AluguelDTO>> listarAlugueis() {
        return ResponseEntity.ok(aluguelService.listarAlugueis());
    }

    @Operation(
        summary = "Buscar anúncio por ID",
        description = "Retorna os dados do anúncio de aluguel correspondente ao ID informado.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Anúncio encontrado com sucesso",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Anúncio não encontrado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AluguelDTO> buscarPorId(
            @Parameter(description = "ID do anúncio de aluguel", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(aluguelService.buscarPorId(id));
    }

    @Operation(
        summary = "Criação de anúncio de aluguel",
        description = "Cria e cadastra um novo anúncio de aluguel.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Anúncio de aluguel criado com sucesso",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados informados", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<AluguelDTO> criarAluguel(
            @Valid @RequestBody AluguelDTO aluguelDTO) {
        return ResponseEntity.ok(aluguelService.criarAluguel(aluguelDTO));
    }

    @Operation(
        summary = "Atualização de anúncio de aluguel",
        description = "Atualiza os dados do anúncio de aluguel, com gerenciamento de imagens.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Anúncio atualizado com sucesso",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Anúncio não encontrado", content = @Content)
        }
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AluguelDTO> atualizarAluguel(
            @Parameter(description = "ID do anúncio de aluguel a ser atualizado", required = true)
            @PathVariable Long id,
            @RequestPart("aluguel") @Valid AluguelDTO aluguelDTO,
            @RequestPart(value = "novasFotos", required = false) MultipartFile[] novasFotos,
            @RequestPart(value = "fotosExistentes", required = false) String fotosExistentesJson
    ) {
        return ResponseEntity.ok(
            aluguelService.atualizarAluguel(id, aluguelDTO, novasFotos, fotosExistentesJson)
        );
    }

    @Operation(
        summary = "Exclusão de anúncio de aluguel",
        description = "Remove o anúncio de aluguel correspondente ao ID informado.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Anúncio deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Anúncio não encontrado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluguel(
            @Parameter(description = "ID do anúncio de aluguel a ser excluído", required = true)
            @PathVariable Long id
    ) {
        aluguelService.deletarAluguel(id);
        return ResponseEntity.noContent().build();
    }

    // === Endpoints de moderação ===

    @Operation(
        summary = "Listar anúncios sinalizados",
        description = "Retorna a lista de anúncios de aluguel sinalizados para moderação."
    )
    @GetMapping("/flagged")
    public ResponseEntity<List<AluguelDTO>> listarFlagged() {
        return ResponseEntity.ok(aluguelService.listarAnunciosSinalizados());
    }

    @Operation(
        summary = "Remover anúncio sinalizado",
        description = "Remove um anúncio sinalizado (moderação)."
    )
    @DeleteMapping("/flagged/{id}")
    public ResponseEntity<Void> removerFlagged(@PathVariable Long id) {
        aluguelService.removeAnuncioSinalizado(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Restaurar anúncio sinalizado",
        description = "Restaura um anúncio sinalizado, limpando a flag.")
    @PostMapping("/flagged/{id}/restore")
    public ResponseEntity<Void> restaurarFlagged(@PathVariable Long id) {
        aluguelService.restaurarAnuncio(id);
        return ResponseEntity.ok().build();
    }
}