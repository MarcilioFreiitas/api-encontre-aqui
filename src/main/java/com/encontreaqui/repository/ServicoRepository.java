package com.encontreaqui.repository;

import com.encontreaqui.model.Servico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    
    // Retorna os serviços cujo título ou categoria contenha a string fornecida (ignora case)
    List<Servico> findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(String titulo, String categoria);
}
