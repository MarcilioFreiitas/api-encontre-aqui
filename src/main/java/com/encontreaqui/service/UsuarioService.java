package com.encontreaqui.service;

import com.encontreaqui.dto.UsuarioDTO;
import com.encontreaqui.mapper.UsuarioMapper;
import com.encontreaqui.model.Usuario;
import com.encontreaqui.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.INSTANCE.toEntity(usuarioDTO);
        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioMapper.INSTANCE.toDTO(salvo);
    }

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return UsuarioMapper.INSTANCE.toDTO(usuario);
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        Usuario usuarioAtualizado = UsuarioMapper.INSTANCE.toEntity(usuarioDTO);
        usuarioAtualizado.setId(usuarioExistente.getId()); // Mantém o ID
        Usuario salvo = usuarioRepository.save(usuarioAtualizado);
        return UsuarioMapper.INSTANCE.toDTO(salvo);
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        usuarioRepository.delete(usuario);
    }
}
