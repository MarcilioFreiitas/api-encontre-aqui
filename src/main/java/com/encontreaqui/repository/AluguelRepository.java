package com.encontreaqui.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encontreaqui.model.Aluguel;


@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    // Adicione consultas específicas para Anúncios de Aluguel, se necessário
}
