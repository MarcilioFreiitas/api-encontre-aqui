package com.encontreaqui.repository;

import com.encontreaqui.model.Comercio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComercioRepository extends JpaRepository<Comercio, Long> {

    // Já existente: query para calcular a média das avaliações para um comércio
    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.tipoItem = 'comercio' AND a.itemId = :comercioId")
    Double findAverageRatingByCommerceId(@Param("comercioId") Long comercioId);
    
    // Novo método para pesquisa: busca comércios cujo título ou categoria contenha o termo (ignorando case)
    List<Comercio> findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(String titulo, String categoria);
}
