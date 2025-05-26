package com.encontreaqui.repository;

import com.encontreaqui.model.Avaliacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    @Query("SELECT a FROM Avaliacao a WHERE a.tipoItem = :tipoItem AND a.itemId = :itemId")
    List<Avaliacao> findByTipoItemAndItemId(@Param("tipoItem") String tipoItem, @Param("itemId") Long itemId);

    // Novo método para listar apenas avaliações sinalizadas
    List<Avaliacao> findByFlaggedTrue();
}