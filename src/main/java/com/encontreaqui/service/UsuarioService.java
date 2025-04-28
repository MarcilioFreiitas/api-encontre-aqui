package com.encontreaqui.service;

import com.encontreaqui.config.JwtTokenUtil;
import com.encontreaqui.dto.LoginDTO;
import com.encontreaqui.dto.ProfileUpdateDTO;
import com.encontreaqui.dto.UsuarioDTO;
import com.encontreaqui.mapper.UsuarioMapper;
import com.encontreaqui.model.Usuario;
import com.encontreaqui.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas operações relacionadas aos usuários: criação, listagem, 
 * atualização completa ou parcial, exclusão e autenticação.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, JwtTokenUtil jwtTokenUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Cria um novo usuário.
     *
     * @param usuarioDTO Dados do usuário a ser criado.
     * @return Dados do usuário recém-criado.
     */
    @Transactional
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = UsuarioMapper.INSTANCE.toEntity(usuarioDTO);
        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioMapper.INSTANCE.toDTO(salvo);
    }

    /**
     * Retorna a lista de todos os usuários cadastrados.
     *
     * @return Lista de UsuarioDTO.
     */
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return Dados do usuário.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return UsuarioMapper.INSTANCE.toDTO(usuario);
    }

    /**
     * Atualiza completamente os dados do usuário.
     * Se a senha for informada, ela é codificada; caso contrário, a senha existente é mantida.
     *
     * @param id         ID do usuário a ser atualizado.
     * @param usuarioDTO Dados completos do usuário.
     * @return Dados do usuário atualizado.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    @Transactional
    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        } else {
            usuarioDTO.setSenha(usuarioExistente.getSenha());
        }
        Usuario usuarioAtualizado = UsuarioMapper.INSTANCE.toEntity(usuarioDTO);
        usuarioAtualizado.setId(usuarioExistente.getId());
        Usuario salvo = usuarioRepository.save(usuarioAtualizado);
        return UsuarioMapper.INSTANCE.toDTO(salvo);
    }

    /**
     * Atualiza parcialmente o perfil do usuário, alterando apenas o nome e o email.
     *
     * @param id               ID do usuário a ser atualizado.
     * @param profileUpdateDTO DTO contendo os campos nome e email.
     * @return Dados do usuário atualizado.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    @Transactional
    public UsuarioDTO atualizarPerfil(Long id, ProfileUpdateDTO profileUpdateDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        usuarioExistente.setNome(profileUpdateDTO.getNome());
        usuarioExistente.setEmail(profileUpdateDTO.getEmail());
        Usuario salvo = usuarioRepository.save(usuarioExistente);
        return UsuarioMapper.INSTANCE.toDTO(salvo);
    }

    /**
     * Remove o usuário de acordo com o ID informado.
     *
     * @param id ID do usuário a ser excluído.
     * @throws RuntimeException se o usuário não for encontrado.
     */
    @Transactional
    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        usuarioRepository.delete(usuario);
    }

    /**
     * Autentica o usuário verificando e-mail e senha e, se bem-sucedido, gera um token JWT.
     *
     * @param loginDTO Dados de login.
     * @return Token JWT.
     * @throws RuntimeException se o usuário não for encontrado ou a senha for inválida.
     */
    public String autenticarUsuario(LoginDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou senha inválida."));
        if (!passwordEncoder.matches(loginDTO.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Usuário não encontrado ou senha inválida.");
        }
        return jwtTokenUtil.generateToken(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }
}
