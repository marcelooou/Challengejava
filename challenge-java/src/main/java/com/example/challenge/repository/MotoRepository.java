package com.example.challenge.repository;

import com.example.challenge.domain.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade Moto.
 * Permite interagir com a tabela MOTO no banco de dados.
 */
@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {

    /**
     * Busca uma moto pelo seu número de Chassi (único).
     * @param chassi O número de chassi da moto.
     * @return Um Optional contendo a moto, se encontrada.
     */
    Optional<Moto> findByChassi(String chassi);

    /**
     * Busca uma moto pela sua Placa (única).
     * @param placa A placa da moto.
     * @return Um Optional contendo a moto, se encontrada.
     */
    Optional<Moto> findByPlaca(String placa);

    /**
     * Busca todas as motos por um determinado status.
     * @param status O status da moto (ex: "Disponível", "Em Manutenção").
     * @return Uma lista de motos com o status especificado.
     */
    List<Moto> findByStatus(String status);
}
