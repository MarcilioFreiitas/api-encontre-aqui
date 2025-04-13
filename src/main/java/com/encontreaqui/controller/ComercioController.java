package com.encontreaqui.controller;

import com.encontreaqui.dto.ComercioDTO;
import com.encontreaqui.service.ComercioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Comércio", description = "Endpoints para gerenciamento de comércios")
@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    @Autowired
    private ComercioService comercioService;

    @Operation(
        summary = "Criar comércio",
        description = "Recebe um objeto ComercioDTO no corpo da requisição e cria um novo comércio."
    )
    @PostMapping
    public ResponseEntity<ComercioDTO> criarComercio(@Valid @RequestBody ComercioDTO comercioDTO) {
        ComercioDTO novoComercio = comercioService.criarComercio(comercioDTO);
        return ResponseEntity.ok(novoComercio);
    }

    @Operation(
        summary = "Listar comércios",
        description = "Retorna a lista de todos os comércios cadastrados."
    )
    @GetMapping
    public ResponseEntity<List<ComercioDTO>> listarComercios() {
        return ResponseEntity.ok(comercioService.listarComercios());
    }

    @Operation(
        summary = "Buscar comércio por ID",
        description = "Recebe o ID do comércio como parâmetro de caminho e retorna os dados do comércio correspondente."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ComercioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comercioService.buscarPorId(id));
    }

    @Operation(
        summary = "Atualizar comércio",
        description = "Recebe o ID do comércio e um objeto ComercioDTO com os novos dados, atualizando o registro correspondente."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ComercioDTO> atualizarComercio(@PathVariable Long id, @Valid @RequestBody ComercioDTO comercioDTO) {
        return ResponseEntity.ok(comercioService.atualizarComercio(id, comercioDTO));
    }

    @Operation(
        summary = "Deletar comércio",
        description = "Recebe o ID do comércio e remove o registro correspondente."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComercio(@PathVariable Long id) {
        comercioService.deletarComercio(id);
        return ResponseEntity.noContent().build();
    }
}
