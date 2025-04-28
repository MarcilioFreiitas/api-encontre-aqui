package com.encontreaqui.controller;

import com.encontreaqui.dto.ServicoDTO;
import com.encontreaqui.service.ServicoService;
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

@Tag(name = "Serviço", description = "Endpoints para o gerenciamento de anúncios de serviço")
@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @Operation(
        summary = "Criação de Serviço",
        description = "Cria um novo anúncio de serviço utilizando os dados fornecidos no corpo da requisição.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Serviço criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServicoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados informados", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<ServicoDTO> criarServico(@Valid @RequestBody ServicoDTO servicoDTO) {
        ServicoDTO novoServico = servicoService.criarServico(servicoDTO);
        return ResponseEntity.ok(novoServico);
    }

    @Operation(
        summary = "Listagem de Serviços",
        description = "Retorna uma lista contendo todos os anúncios de serviço cadastrados no sistema."
    )
    @GetMapping
    public ResponseEntity<List<ServicoDTO>> listarServicos() {
        List<ServicoDTO> servicos = servicoService.listarServicos();
        return ResponseEntity.ok(servicos);
    }

    @Operation(
        summary = "Busca de Serviço por ID",
        description = "Retorna os detalhes do anúncio de serviço correspondente ao ID informado.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Serviço encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServicoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> buscarPorId(
            @Parameter(description = "ID do anúncio de serviço", required = true)
            @PathVariable Long id) {
        ServicoDTO servicoDTO = servicoService.buscarPorId(id);
        return ResponseEntity.ok(servicoDTO);
    }

    @Operation(
        summary = "Atualização de Serviço",
        description = "Atualiza os dados do anúncio de serviço correspondente ao ID informado, permitindo gerir as imagens (removendo as existentes e adicionando novas). " +
                      "Este endpoint consome multipart/form-data onde: " +
                      "<br>- A parte 'servico' contém os dados do serviço em JSON; " +
                      "<br>- A parte 'fotosExistentes' (opcional) contém um JSON com os caminhos das fotos a serem mantidas; " +
                      "<br>- A parte 'novasFotos' (opcional) contém os arquivos das novas fotos.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServicoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
        }
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServicoDTO> atualizarServico(
            @Parameter(description = "ID do anúncio de serviço a ser atualizado", required = true)
            @PathVariable Long id,
            @RequestPart("servico") @Valid ServicoDTO servicoDTO,
            @RequestPart(value = "novasFotos", required = false) MultipartFile[] novasFotos,
            @RequestPart(value = "fotosExistentes", required = false) String fotosExistentesJson) {
        ServicoDTO servicoAtualizado = servicoService.atualizarServico(id, servicoDTO, novasFotos, fotosExistentesJson);
        return ResponseEntity.ok(servicoAtualizado);
    }

    @Operation(
        summary = "Exclusão de Serviço",
        description = "Remove o anúncio de serviço correspondente ao ID informado.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Serviço excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(
            @Parameter(description = "ID do anúncio de serviço a ser excluído", required = true)
            @PathVariable Long id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }
}
