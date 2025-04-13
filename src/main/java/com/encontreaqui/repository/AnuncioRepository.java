package com.encontreaqui.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encontreaqui.model.Anuncio;


@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long> {
    // Métodos customizados de consulta podem ser acrescentados aqui
}
