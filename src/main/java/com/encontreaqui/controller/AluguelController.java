package com.encontreaqui.controller;

import com.encontreaqui.dto.AluguelDTO;
import com.encontreaqui.service.AluguelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Aluguel", description = "Endpoints para gerenciamento de anúncios de aluguel")
@RestController
@RequestMapping("/api/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    @Operation(
        summary = "Criação de anúncio de aluguel",
        description = "Cria e cadastra um novo anúncio de aluguel utilizando os dados fornecidos em AluguelDTO.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Anúncio de aluguel criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados informados", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<AluguelDTO> criarAluguel(@Valid @RequestBody AluguelDTO aluguelDTO) {
        AluguelDTO novoAluguel = aluguelService.criarAluguel(aluguelDTO);
        return ResponseEntity.ok(novoAluguel);
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
        summary = "Busca de anúncio de aluguel por ID",
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
        summary = "Atualização de anúncio de aluguel",
        description = "Atualiza os dados do anúncio de aluguel correspondente ao ID fornecido com as informações do AluguelDTO.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Anúncio atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AluguelDTO.class))),
            @ApiResponse(responseCode = "404", description = "Anúncio não encontrado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AluguelDTO> atualizarAluguel(
            @Parameter(description = "ID do anúncio de aluguel a ser atualizado", required = true)
            @PathVariable Long id,
            @Valid @RequestBody AluguelDTO aluguelDTO) {
        return ResponseEntity.ok(aluguelService.atualizarAluguel(id, aluguelDTO));
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
            @PathVariable Long id) {
        aluguelService.deletarAluguel(id);
        return ResponseEntity.noContent().build();
    }
}
