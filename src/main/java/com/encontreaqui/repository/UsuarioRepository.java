package com.encontreaqui.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encontreaqui.model.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Aqui você pode definir métodos de consulta customizados, por exemplo:
    // Optional<Usuario> findByEmail(String email);
}
