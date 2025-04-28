package com.encontreaqui.controller;

import com.encontreaqui.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/imagens")
@Tag(name = "Imagem", description = "Endpoints para upload e gerenciamento de imagens")
public class ImagemController {

    @Autowired
    private FileStorageService fileStorageService;

    @Operation(
        summary = "Upload de Imagem",
        description = "Recebe um arquivo via MultipartFile, realiza o upload e retorna o URI de acesso à imagem salva no servidor.",
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Imagem salva com sucesso",
                content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Erro ao processar o upload da imagem", 
                content = @Content
            )
        }
    )
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImagem(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();
        Map<String, String> response = new HashMap<>();
        response.put("url", fileDownloadUri);
        return ResponseEntity.ok(response);
    }

}
