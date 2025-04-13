package com.encontreaqui.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
public class Comercio extends Anuncio {

    @NotNull(message = "O endereço completo não pode ser nulo.")
    @Size(min = 10, max = 255, message = "O endereço completo deve ter entre 10 e 255 caracteres.")
    private String enderecoCompleto;

    @NotNull(message = "O horário de funcionamento não pode ser nulo.")
    @Pattern(regexp = "^\\d{2}:\\d{2}-\\d{2}:\\d{2}$", message = "O horário de funcionamento deve estar no formato HH:MM-HH:MM.")
    private String horarioFuncionamento;

    @NotNull(message = "O telefone não pode ser nulo.")
    @Pattern(regexp = "\\+\\d{2}\\s\\d{9}", message = "O telefone deve estar no formato +DD DDDDDDDDD.")
    private String telefone;

    @Size(max = 255, message = "O website deve ter no máximo 255 caracteres.")
    private String website;

    @NotNull(message = "O tipo de estabelecimento não pode ser nulo.")
    @Size(min = 3, max = 100, message = "O tipo de estabelecimento deve ter entre 3 e 100 caracteres.")
    private String tipoEstabelecimento;

    public Comercio(String enderecoCompleto, String horarioFuncionamento, String telefone, String website,
                    String tipoEstabelecimento) {
        super();
        this.enderecoCompleto = enderecoCompleto;
        this.horarioFuncionamento = horarioFuncionamento;
        this.telefone = telefone;
        this.website = website;
        this.tipoEstabelecimento = tipoEstabelecimento;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTipoEstabelecimento() {
        return tipoEstabelecimento;
    }

    public void setTipoEstabelecimento(String tipoEstabelecimento) {
        this.tipoEstabelecimento = tipoEstabelecimento;
    }
}
