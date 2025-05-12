package com.encontreaqui.repository;

import com.encontreaqui.model.Avaliacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Retorna todas as avaliações para um item específico, de um determinado tipo
    @Query("SELECT a FROM Avaliacao a WHERE a.tipoItem = :tipoItem AND a.itemId = :itemId")
    List<Avaliacao> findByTipoItemAndItemId(@Param("tipoItem") String tipoItem, @Param("itemId") Long itemId);
}
