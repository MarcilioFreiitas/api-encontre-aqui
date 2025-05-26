package com.encontreaqui.controller;

import com.encontreaqui.dto.ComercioDTO;
import com.encontreaqui.service.ComercioService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Comércio", description = "Endpoints para gerenciamento de comércios e moderação")
@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    @Autowired
    private ComercioService comercioService;

    @Operation(
        summary = "Criar comércio",
        description = "Cria um novo comércio com dados fornecidos em ComercioDTO."
    )
    @ApiResponse(responseCode = "200", description = "Comércio criado com sucesso",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComercioDTO.class)))
    @PostMapping
    public ResponseEntity<ComercioDTO> criarComercio(@Valid @RequestBody ComercioDTO comercioDTO) {
        return ResponseEntity.ok(comercioService.criarComercio(comercioDTO));
    }

    @Operation(
        summary = "Listar comércios",
        description = "Retorna todos os comércios cadastrados, com média de avaliações."
    )
    @GetMapping
    public ResponseEntity<List<ComercioDTO>> listarComercios() {
        return ResponseEntity.ok(comercioService.listarComercios());
    }

    @Operation(
        summary = "Buscar comércio por ID",
        description = "Retorna o comércio correspondente ao ID informado."
    )
    @ApiResponse(responseCode = "200", description = "Comércio encontrado",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComercioDTO.class)))
    @GetMapping("/{id}")
    public ResponseEntity<ComercioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comercioService.buscarPorId(id));
    }

    @Operation(
        summary = "Atualizar comércio",
        description = "Atualiza os dados do comércio e gerencia fotos existentes e novas."
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ComercioDTO> atualizarComercio(
            @PathVariable Long id,
            @RequestPart("comercio") @Valid ComercioDTO comercioDTO,
            @RequestPart(value = "novasFotos", required = false) MultipartFile[] novasFotos,
            @RequestPart(value = "fotosExistentes", required = false) String fotosExistentesJson
    ) {
        return ResponseEntity.ok(
            comercioService.atualizarComercio(id, comercioDTO, novasFotos, fotosExistentesJson)
        );
    }

    @Operation(
        summary = "Deletar comércio",
        description = "Remove o comércio correspondente ao ID informado."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComercio(@PathVariable Long id) {
        comercioService.deletarComercio(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Pesquisar comércio",
        description = "Busca comércios por título ou categoria, ignorando case."
    )
    @GetMapping("/search")
    public ResponseEntity<List<ComercioDTO>> search(@RequestParam("q") String query) {
        return ResponseEntity.ok(comercioService.searchComercios(query));
    }

    // ==== Endpoints de moderação ====

    @Operation(
        summary = "Listar comércios sinalizados",
        description = "Retorna comércios sinalizados para moderação."
    )
    @GetMapping("/flagged")
    public ResponseEntity<List<ComercioDTO>> listarFlagged() {
        return ResponseEntity.ok(comercioService.listarComerciosSinalizados());
    }

    @Operation(
        summary = "Remover comércio sinalizado",
        description = "Remove o comércio sinalizado (moderação)."
    )
    @ApiResponse(responseCode = "204", description = "Comércio sinalizado removido com sucesso")
    @DeleteMapping("/flagged/{id}")
    public ResponseEntity<Void> removerFlagged(@PathVariable Long id) {
        comercioService.removeComercioSinalizado(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Restaurar comércio sinalizado",
        description = "Restaura o comércio sinalizado, limpando a sinalização."
    )
    @PostMapping("/flagged/{id}/restore")
    public ResponseEntity<Void> restaurarFlagged(@PathVariable Long id) {
        comercioService.restaurarComercio(id);
        return ResponseEntity.ok().build();
    }
}
