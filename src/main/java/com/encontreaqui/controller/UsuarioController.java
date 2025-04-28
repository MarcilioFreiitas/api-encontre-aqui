package com.encontreaqui.controller;

import com.encontreaqui.dto.LoginDTO;
import com.encontreaqui.dto.TokenResponseDTO;
import com.encontreaqui.dto.UsuarioDTO;
import com.encontreaqui.dto.ProfileUpdateDTO;
import com.encontreaqui.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operações relacionadas a Usuário.
 */
@Tag(name = "Usuário", description = "Endpoints para gerenciamento, registro, login, atualização (completa e parcial) e exclusão de usuários")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
        summary = "Registro de Usuário",
        description = "Cria um novo usuário utilizando os dados fornecidos no corpo da requisição.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content)
        }
    )
    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> criarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO novoUsuario = usuarioService.criarUsuario(usuarioDTO);
        return ResponseEntity.status(201).body(novoUsuario);
    }

    @Operation(
        summary = "Login de Usuário",
        description = "Realiza a autenticação do usuário com base no e-mail e senha fornecidos.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas", content = @Content)
        }
    )
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> autenticarUsuario(@Valid @RequestBody LoginDTO loginDTO) {
        String token = usuarioService.autenticarUsuario(loginDTO);
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }

    @Operation(
        summary = "Listagem de Usuários",
        description = "Retorna uma lista com todos os usuários cadastrados.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso",
                         content = @Content(mediaType = "application/json"))
        }
    )
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(
        summary = "Buscar Usuário por ID",
        description = "Retorna os dados do usuário correspondente ao ID fornecido.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(
            @Parameter(description = "ID do usuário a ser buscado", required = true)
            @PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(
        summary = "Atualização Completa de Usuário",
        description = "Atualiza todos os dados do usuário, incluindo a senha se informada.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(
            @Parameter(description = "ID do usuário a ser atualizado", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @Operation(
        summary = "Atualização Parcial do Perfil",
        description = "Atualiza apenas o nome e o email do usuário, via atualização parcial (PATCH).",
        responses = {
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso",
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
        }
    )
    @PatchMapping("/{id}/profile")
    public ResponseEntity<UsuarioDTO> atualizarPerfil(
            @Parameter(description = "ID do usuário cujo perfil será atualizado", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ProfileUpdateDTO profileUpdateDTO) {
        UsuarioDTO usuarioAtualizado = usuarioService.atualizarPerfil(id, profileUpdateDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @Operation(
        summary = "Exclusão de Usuário",
        description = "Remove o usuário correspondente ao ID informado.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(
            @Parameter(description = "ID do usuário a ser deletado", required = true)
            @PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
