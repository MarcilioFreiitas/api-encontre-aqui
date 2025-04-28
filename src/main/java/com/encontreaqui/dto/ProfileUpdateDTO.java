package com.encontreaqui.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para atualização parcial do perfil do usuário. Permite alterar somente o nome e o email.
 */
public class ProfileUpdateDTO {

    @NotBlank(message = "Nome não pode ser vazio.")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "Email não pode ser vazio.")
    @Email(message = "Email deve ser válido.")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres.")
    private String email;

    // Getters e Setters
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
}
