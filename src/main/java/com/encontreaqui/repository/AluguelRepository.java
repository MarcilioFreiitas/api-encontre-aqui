package com.encontreaqui.repository;

import com.encontreaqui.model.Aluguel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    // Método customizado que busca anúncios de aluguel cujo título ou categoria contenha o termo (ignora case)
    List<Aluguel> findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(String titulo, String categoria);
    List<Aluguel> findByFlaggedTrue();
}
