package com.encontreaqui.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "fotos")
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Caminho ou URL da imagem no servidor
    @NotNull(message = "O caminho da imagem não pode ser nulo.")
    private String caminho;

    // Associação opcional com Comércio
    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Comercio comercio;

    // Associação opcional com Aluguel
    @ManyToOne
    @JoinColumn(name = "aluguel_id")
    private Aluguel aluguel;

    // Associação opcional com Serviço
    @ManyToOne
    @JoinColumn(name = "servico_id")
    private Servico servico;

    public Foto() {}

    public Foto(String caminho, Comercio comercio) {
        this.caminho = caminho;
        this.comercio = comercio;
    }

    public Foto(String caminho, Aluguel aluguel) {
        this.caminho = caminho;
        this.aluguel = aluguel;
    }

    public Foto(String caminho, Servico servico) {
        this.caminho = caminho;
        this.servico = servico;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public Aluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(Aluguel aluguel) {
        this.aluguel = aluguel;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }
}
