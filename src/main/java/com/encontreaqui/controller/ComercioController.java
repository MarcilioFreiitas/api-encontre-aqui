package com.encontreaqui.controller;

import com.encontreaqui.dto.ComercioDTO;
import com.encontreaqui.service.ComercioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
        description = "Recebe um objeto ComercioDTO no corpo da requisição (JSON) e cria um novo comércio."
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
        description = "Recebe o ID do comércio e retorna os dados do comércio correspondente."
    )
    @GetMapping("/{id}")
    public ResponseEntity<ComercioDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comercioService.buscarPorId(id));
    }

    @Operation(
        summary = "Atualizar comércio com gerenciamento de fotos",
        description = "Atualiza os dados do comércio, permitindo editar campos e gerenciar fotos – removendo fotos existentes e/ou adicionando novas. " +
                      "O endpoint consome multipart/form-data, onde: " +
                      "<br>- A parte 'comercio' contém os dados do comércio em JSON; " +
                      "<br>- A parte 'fotosExistentes' (opcional) contém um JSON com os caminhos a serem mantidos; " +
                      "<br>- A parte 'novasFotos' (opcional) contém os arquivos das novas fotos."
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ComercioDTO> atualizarComercio(
            @PathVariable Long id,
            @RequestPart("comercio") @Valid ComercioDTO comercioDTO,
            @RequestPart(value = "novasFotos", required = false) MultipartFile[] novasFotos,
            @RequestPart(value = "fotosExistentes", required = false) String fotosExistentesJson) {
        ComercioDTO atualizado = comercioService.atualizarComercio(id, comercioDTO, novasFotos, fotosExistentesJson);
        return ResponseEntity.ok(atualizado);
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
    
    @Operation(
        summary = "Pesquisar comércio por título ou categoria",
        description = "Retorna os comércios cujo título ou categoria contenha o termo de pesquisa, ignorando maiúsculas/minúsculas."
    )
    @GetMapping("/search")
    public ResponseEntity<List<ComercioDTO>> search(@RequestParam("q") String query) {
        List<ComercioDTO> result = comercioService.searchComercios(query);
        return ResponseEntity.ok(result);
    }
}
