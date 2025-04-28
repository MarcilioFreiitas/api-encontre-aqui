package com.encontreaqui.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role; // Ex.: ADMIN, COMERCIANTE, CLIENTE

    // Construtor padrão
    public Usuario() { }

    // Construtor parametrizado
    public Usuario(Long id, String nome, String email, String senha, Role role) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
  
    public String getEmail() {
        return email;
    }
  
    public void setEmail(String email) {
        this.email = email;
    }
  
    public String getSenha() {
        return senha;
    }
  
    public void setSenha(String senha) {
        this.senha = senha;
    }
  
    public Role getRole() {
        return role;
    }
  
    public void setRole(Role role) {
        this.role = role;
    }
}
