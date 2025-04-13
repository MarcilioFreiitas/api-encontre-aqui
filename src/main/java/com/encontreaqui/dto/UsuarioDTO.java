package com.encontreaqui.dto;

public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String role; // Pode ser utilizado como String ou convertê-lo para um Enum

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
}
