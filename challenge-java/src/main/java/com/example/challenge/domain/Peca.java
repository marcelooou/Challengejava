package com.example.challenge.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data // Mantém esta anotação para getters e setters automáticos
@Entity
public class Peca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    
    // CAMPOS ADICIONADOS PARA CORRESPONDER AO PecaDTO E CORRIGIR A COMPILAÇÃO
    private String descricao;
    private Double preco;
    // FIM DOS CAMPOS ADICIONADOS

    private String codigoFabricante;
    private int estoqueAtual;
    private int estoqueMinimo;
    private String localizacaoEstoque;
}