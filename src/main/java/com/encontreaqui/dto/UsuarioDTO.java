package com.encontreaqui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String role;
    
    // O campo senha é write-only: será considerado na criação/atualização, mas não aparece nas respostas
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    // Construtor padrão
    public UsuarioDTO() { }

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
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
