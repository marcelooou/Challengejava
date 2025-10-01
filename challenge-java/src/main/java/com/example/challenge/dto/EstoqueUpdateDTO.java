package com.example.challenge.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO para representar a quantidade de alteração de estoque.
 * A validação é simples, pois o Service garante que o estoque final não seja negativo.
 */
public class EstoqueUpdateDTO {

    @NotNull(message = "A quantidade de alteração é obrigatória.")
    // Não usamos @Min(1) ou @Max(n) pois o valor pode ser POSITIVO (entrada) ou NEGATIVO (saída)
    public Integer quantidade;

    // Construtor, Getters e Setters

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
