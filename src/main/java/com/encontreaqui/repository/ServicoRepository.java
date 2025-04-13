package com.encontreaqui.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encontreaqui.model.Servico;


@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    // Adicione métodos customizados para buscar serviços
}
