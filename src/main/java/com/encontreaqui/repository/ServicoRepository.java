package com.encontreaqui.repository;

import com.encontreaqui.model.Servico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositório para operações de Serviço, incluindo busca, média de avaliações e moderação.
 */
@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    /**
     * Calcula a média das avaliações (nota) para um serviço específico.
     */
    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.tipoItem = 'servico' AND a.itemId = :servicoId")
    Double findAverageRatingByServiceId(@Param("servicoId") Long servicoId);

    /**
     * Busca serviços cujo título ou categoria contenha o termo (ignora case).
     */
    List<Servico> findByTituloContainingIgnoreCaseOrCategoriaContainingIgnoreCase(String titulo, String categoria);

    /**
     * Retorna todos os serviços sinalizados para moderação.
     */
    List<Servico> findByFlaggedTrue();
}
