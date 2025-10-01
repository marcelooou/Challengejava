package com.example.challenge.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para criação e atualização da entidade Moto.
 * Garante que os dados de identificação e status da moto sejam válidos antes
 * de serem processados pelo Service.
 */
public class MotoDTO {

    @NotBlank(message = "O número do chassi é obrigatório.")
    @Size(min = 17, max = 17, message = "O chassi deve ter exatamente 17 caracteres.")
    public String chassi;

    @NotBlank(message = "A placa é obrigatória.")
    @Size(min = 7, max = 7, message = "A placa deve ter 7 caracteres (considerando o novo padrão Mercosul).")
    public String placa;

    @NotBlank(message = "O modelo é obrigatório.")
    @Size(max = 50, message = "O modelo não pode exceder 50 caracteres.")
    public String modelo;

    @NotNull(message = "O KM atual é obrigatório.")
    @Min(value = 0, message = "O KM atual não pode ser negativo.")
    public Integer kmAtual;

    @NotBlank(message = "O status é obrigatório.")
    @Size(max = 30, message = "O status não pode exceder 30 caracteres.")
    // Ex: "Disponível", "Em Manutenção", "Alocada"
    public String status;

    @NotNull(message = "O ano de fabricação é obrigatório.")
    // Uma validação customizada mais complexa poderia ser usada para verificar o ano,
    // mas o Min é suficiente por enquanto.
    @Min(value = 2000, message = "O ano de fabricação deve ser 2000 ou superior.")
    public Integer anoFabricacao;

    // Getters e Setters (Boas Práticas)

    public String getChassi() { return chassi; }
    public void setChassi(String chassi) { this.chassi = chassi; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public Integer getKmAtual() { return kmAtual; }
    public void setKmAtual(Integer kmAtual) { this.kmAtual = kmAtual; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(Integer anoFabricacao) { this.anoFabricacao = anoFabricacao; }
}
