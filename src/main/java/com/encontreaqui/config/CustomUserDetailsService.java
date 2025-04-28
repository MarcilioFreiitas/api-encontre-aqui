package com.encontreaqui.config;

import com.encontreaqui.model.Usuario;
import com.encontreaqui.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(username);
        if (!usuarioOpt.isPresent()) {
            throw new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username);
        }
        Usuario usuario = usuarioOpt.get();

        // Configura o UserDetails usando o e-mail e a senha criptografada
        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(usuario.getRole().name())
                .build();
    }
}
