package com.encontreaqui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.encontreaqui.model.Comercio;


@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long> {
    // Adicione consultas específicas para Comércio, se necessário
}
