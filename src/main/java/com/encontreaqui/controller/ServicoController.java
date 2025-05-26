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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Serviço", description = "Endpoints para o gerenciamento de anúncios de serviço")
@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    @Autowired
    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @Operation(
        summary = "Pesquisar serviços",
        description = "Retorna os serviços cujo título ou categoria contenha o termo de pesquisa (ignorando maiúsculas/minúsculas)."
    )
    @ApiResponse(responseCode = "200", description = "Lista de serviços retornada com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServicoDTO.class)))
    @GetMapping("/search")
    public ResponseEntity<List<ServicoDTO>> search(
            @Parameter(description = "Termo de pesquisa", required = true)
            @RequestParam("q") String query) {
        List<ServicoDTO> result = servicoService.searchServicos(query);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Listar serviços",
        description = "Retorna todos os anúncios de serviço cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista de serviços retornada com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServicoDTO.class)))
    @GetMapping
    public ResponseEntity<List<ServicoDTO>> listarServicos() {
        List<ServicoDTO> servicos = servicoService.listarServicos();
        return ResponseEntity.ok(servicos);
    }

    @Operation(
        summary = "Buscar serviço por ID",
        description = "Retorna os detalhes de um anúncio de serviço pelo ID informado."
    )
    @ApiResponse(responseCode = "200", description = "Serviço encontrado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServicoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> buscarPorId(
            @Parameter(description = "ID do anúncio de serviço", required = true)
            @PathVariable Long id) {
        ServicoDTO dto = servicoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
        summary = "Criar serviço",
        description = "Cria um novo anúncio de serviço com os dados fornecidos."
    )
    @ApiResponse(responseCode = "200", description = "Serviço criado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServicoDTO.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    @PostMapping
    public ResponseEntity<ServicoDTO> criarServico(
            @Parameter(description = "Dados do serviço a ser criado", required = true)
            @Valid @RequestBody ServicoDTO servicoDTO) {
        ServicoDTO criado = servicoService.criarServico(servicoDTO);
        return ResponseEntity.ok(criado);
    }

    @Operation(
        summary = "Atualizar serviço",
        description = "Atualiza um anúncio de serviço existente, incluindo gestão de fotos (remover e adicionar)."
    )
    @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServicoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServicoDTO> atualizarServico(
            @Parameter(description = "ID do anúncio de serviço a ser atualizado", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do serviço", required = true)
            @RequestPart("servico") @Valid ServicoDTO servicoDTO,
            @Parameter(description = "Novas fotos a serem adicionadas", required = false)
            @RequestPart(value = "novasFotos", required = false) MultipartFile[] novasFotos,
            @Parameter(description = "JSON com caminhos das fotos existentes a manter", required = false)
            @RequestPart(value = "fotosExistentes", required = false) String fotosExistentesJson) {
        ServicoDTO atualizado = servicoService.atualizarServico(id, servicoDTO, novasFotos, fotosExistentesJson);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(
        summary = "Deletar serviço",
        description = "Remove um anúncio de serviço pelo ID informado."
    )
    @ApiResponse(responseCode = "204", description = "Serviço excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(
            @Parameter(description = "ID do anúncio de serviço a ser excluído", required = true)
            @PathVariable Long id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }

    // --- Novos endpoints de moderação ---

    @Operation(
        summary = "Sinalizar (flag) serviço",
        description = "Marca um serviço como sinalizado, armazenando a razão."
    )
    @ApiResponse(responseCode = "204", description = "Serviço sinalizado com sucesso")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/flag")
    public ResponseEntity<Void> flagServico(
            @Parameter(description = "ID do serviço a ser sinalizado", required = true)
            @PathVariable Long id,
            @Parameter(description = "Motivo da sinalização", required = true)
            @RequestParam("reason") String reason) {
        servicoService.flagServico(id, reason);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Remover sinalização (unflag) de serviço",
        description = "Remove a sinalização de um serviço previamente sinalizado."
    )
    @ApiResponse(responseCode = "204", description = "Serviço dessinalizado com sucesso")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/unflag")
    public ResponseEntity<Void> unflagServico(
            @Parameter(description = "ID do serviço a ter sinalização removida", required = true)
            @PathVariable Long id) {
        servicoService.unflagServico(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Listar serviços sinalizados",
        description = "Retorna todos os serviços que estão sinalizados para moderação."
    )
    @ApiResponse(responseCode = "200", description = "Lista de serviços sinalizados retornada com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServicoDTO.class)))
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/flagged")
    public ResponseEntity<List<ServicoDTO>> listarFlagged() {
        List<ServicoDTO> flagged = servicoService.listarServicosFlagged();
        return ResponseEntity.ok(flagged);
    }
}
