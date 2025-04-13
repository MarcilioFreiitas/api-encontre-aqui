package com.encontreaqui.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encontreaqui.model.Foto;


@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {
    // Métodos customizados podem ser definidos aqui conforme necessário
}
