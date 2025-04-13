package com.encontreaqui.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class Servico extends Anuncio {

    @NotNull(message = "A área de atuação não pode ser nula.")
    @Size(min = 3, max = 100, message = "A área de atuação deve ter entre 3 e 100 caracteres.")
    private String areaAtuacao;

    @NotNull(message = "O tempo médio de atendimento não pode ser nulo.")
    @Positive(message = "O tempo médio de atendimento deve ser um número positivo.")
    private Integer tempoMedioAtendimento;

    @NotNull(message = "A indicação de agendamento prévio não pode ser nula.")
    private boolean necessitaAgendamento;

    @NotNull(message = "O nome do profissional responsável não pode ser nulo.")
    @Size(min = 3, max = 100, message = "O nome do profissional responsável deve ter entre 3 e 100 caracteres.")
    private String profissionalResponsavel;

    public Servico(String areaAtuacao, Integer tempoMedioAtendimento, boolean necessitaAgendamento,
                   String profissionalResponsavel) {
        super();
        this.areaAtuacao = areaAtuacao;
        this.tempoMedioAtendimento = tempoMedioAtendimento;
        this.necessitaAgendamento = necessitaAgendamento;
        this.profissionalResponsavel = profissionalResponsavel;
    }

    public String getAreaAtuacao() {
        return areaAtuacao;
    }

    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }

    public Integer getTempoMedioAtendimento() {
        return tempoMedioAtendimento;
    }

    public void setTempoMedioAtendimento(Integer tempoMedioAtendimento) {
        this.tempoMedioAtendimento = tempoMedioAtendimento;
    }

    public boolean isNecessitaAgendamento() {
        return necessitaAgendamento;
    }

    public void setNecessitaAgendamento(boolean necessitaAgendamento) {
        this.necessitaAgendamento = necessitaAgendamento;
    }

    public String getProfissionalResponsavel() {
        return profissionalResponsavel;
    }

    public void setProfissionalResponsavel(String profissionalResponsavel) {
        this.profissionalResponsavel = profissionalResponsavel;
    }
}
