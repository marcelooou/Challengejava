package com.example.challenge.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotNull;
import lombok.Data; // Importação essencial do Lombok
import java.time.LocalDateTime; // Importação para dataAbertura

@Data // Anotação essencial para gerar getMoto() e setMoto()
@Entity
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String status; // Ex: ABERTA, EM_SERVICO, CONCLUIDA

    private String descricao;
    
    // CAMPOS ADICIONADOS PARA RESOLVER ERROS setDescricaoProblema/setDataAbertura NO TESTE
    private String descricaoProblema;
    private LocalDateTime dataAbertura; 

    // Este campo é essencial para a relação e para os métodos get/set
    @ManyToOne
    @JoinColumn(name = "moto_id", nullable = false)
    private Moto moto; 
    
    // Outros campos podem ser adicionados...
}
    