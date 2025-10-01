package com.example.challenge.repository;

import com.example.challenge.domain.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para a entidade OrdemServico.
 * Permite interagir com a tabela ORDEM_SERVICO no banco de dados.
 */
@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    /**
     * Busca ordens de serviço por um status específico.
     * @param status O status da ordem (Ex: "Aberta", "Em Andamento", "Concluída").
     * @return Lista de ordens de serviço com o status correspondente.
     */
    List<OrdemServico> findByStatus(String status);
    
    /**
     * Busca ordens de serviço associadas a uma Moto específica (usando o ID da Moto).
     * @param motoId O ID da moto.
     * @return Lista de ordens de serviço para aquela moto.
     */
    List<OrdemServico> findByMotoId(Long motoId);
}
