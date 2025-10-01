package com.example.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * PecaDTO (Data Transfer Object)
 * Utilizado para transferência de dados entre o controlador e o serviço,
 * e para validação de entrada de dados (requests).
 *
 * Usa Lombok para gerar boilerplate code (getters, setters, construtores, etc.).
 */
@Data // Inclui @Getter, @Setter, @ToString, @EqualsAndHashCode, e @RequiredArgsConstructor
@Builder // Permite a construção do objeto usando o padrão Builder
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os argumentos
public class PecaDTO {

    private Long id; // ID da peça, normalmente não preenchido na entrada (input)

    @NotBlank(message = "O nome da peça é obrigatório.")
    private String nome;

    @NotBlank(message = "A descrição da peça é obrigatória.")
    private String descricao;
    
    @NotBlank(message = "O código do fabricante é obrigatório.")
    private String codigoFabricante;

    @NotNull(message = "O preço da peça é obrigatório.")
    @Min(value = 0, message = "O preço não pode ser negativo.")
    private Double preco;

    @NotNull(message = "O estoque atual é obrigatório.")
    @Min(value = 0, message = "O estoque atual não pode ser negativo.")
    private Integer estoqueAtual;
    
    @NotNull(message = "O estoque mínimo é obrigatório.")
    @Min(value = 0, message = "O estoque mínimo não pode ser negativo.")
    private Integer estoqueMinimo;
    
    @NotBlank(message = "A localização no estoque é obrigatória.")
    private String localizacaoEstoque;
}