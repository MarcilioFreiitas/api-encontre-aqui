package com.encontreaqui.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // URL da foto ou caminho onde ela está armazenada
    private String url;

    // Relacionamento com o anúncio
    @ManyToOne
    @JoinColumn(name = "anuncio_id")
    private Anuncio anuncio;

    // Getters e Setters
}
