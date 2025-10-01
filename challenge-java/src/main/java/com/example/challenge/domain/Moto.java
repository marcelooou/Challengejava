package com.example.challenge.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma motocicleta na frota da Mottu.
 * Usada para gestão de inventário e rastreamento.
 */
@Entity
@Table(name = "MOTO")
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CHASSI", nullable = false, unique = true, length = 50)
    private String chassi; // Número de identificação único da moto

    @Column(name = "MODELO", nullable = false, length = 100)
    private String modelo; // Ex: Honda Pop 110i, Biz 125

    @Column(name = "PLACA", nullable = false, unique = true, length = 10)
    private String placa;

    @Column(name = "ANO_FABRICACAO")
    private Integer anoFabricacao;

    // Status da moto: Disponível, Em Manutenção, Em Rota, Vendida
    @Column(name = "STATUS", nullable = false, length = 30)
    private String status;

    @Column(name = "KM_ATUAL")
    private Integer kmAtual;

    @Column(name = "DATA_CADASTRO", nullable = false)
    private LocalDateTime dataCadastro;

    // Construtor, Getters e Setters

    public Moto() {
        this.dataCadastro = LocalDateTime.now();
        this.status = "Disponível";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getChassi() { return chassi; }
    public void setChassi(String chassi) { this.chassi = chassi; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public Integer getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(Integer anoFabricacao) { this.anoFabricacao = anoFabricacao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getKmAtual() { return kmAtual; }
    public void setKmAtual(Integer kmAtual) { this.kmAtual = kmAtual; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}
