package com.encontreaqui.controller;

import com.encontreaqui.model.Foto;
import com.encontreaqui.service.FotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Foto", description = "Endpoints para o gerenciamento de fotos")
@RestController
@RequestMapping("/api/fotos")
public class FotoController {

    @Autowired
    private FotoService fotoService;

    @Operation(
        summary = "Criação de Foto",
        description = "Cria uma nova foto com base nos dados fornecidos no corpo da requisição.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Foto criada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Foto.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<Foto> criarFoto(@Valid @RequestBody Foto foto) {
        Foto novaFoto = fotoService.criarFoto(foto);
        return ResponseEntity.ok(novaFoto);
    }

    @Operation(
        summary = "Listagem de Fotos",
        description = "Retorna uma lista com todas as fotos cadastradas."
    )
    @GetMapping
    public ResponseEntity<List<Foto>> listarFotos() {
        return ResponseEntity.ok(fotoService.listarFotos());
    }

    @Operation(
        summary = "Buscar Foto por ID",
        description = "Retorna os detalhes de uma foto específica com base no ID fornecido.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Foto encontrada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Foto.class))),
            @ApiResponse(responseCode = "404", description = "Foto não encontrada", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Foto> buscarPorId(
            @Parameter(description = "ID da foto a ser buscada", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(fotoService.buscarPorId(id));
    }

    @Operation(
        summary = "Exclusão de Foto",
        description = "Exclui a foto correspondente ao ID informado.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Foto excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Foto não encontrada", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFoto(
            @Parameter(description = "ID da foto a ser excluída", required = true)
            @PathVariable Long id) {
        fotoService.deletarFoto(id);
        return ResponseEntity.noContent().build();
    }
}
