package com.example.challenge.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para criação e atualização da entidade OrdemServico.
 * Garante a validação dos campos, como a obrigatoriedade da ID da moto e a descrição.
 */
public class OrdemServicoDTO {

    @NotNull(message = "O ID da moto é obrigatório.")
    @Min(value = 1, message = "O ID da moto deve ser maior que zero.")
    public Long motoId;

    @NotBlank(message = "A descrição do problema é obrigatória.")
    @Size(max = 500, message = "A descrição não pode exceder 500 caracteres.")
    public String descricaoProblema;

    @NotBlank(message = "O status é obrigatório.")
    @Size(max = 30, message = "O status não pode exceder 30 caracteres.")
    // Ex: "Aberta", "Em Andamento", "Concluída"
    public String status;

    @NotNull(message = "A data de abertura é obrigatória.")
    public LocalDateTime dataAbertura;

    // A data de conclusão não é obrigatória na criação

    // Getters e Setters

    public Long getMotoId() { return motoId; }
    public void setMotoId(Long motoId) { this.motoId = motoId; }
    public String getDescricaoProblema() { return descricaoProblema; }
    public void setDescricaoProblema(String descricaoProblema) { this.descricaoProblema = descricaoProblema; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }
}
