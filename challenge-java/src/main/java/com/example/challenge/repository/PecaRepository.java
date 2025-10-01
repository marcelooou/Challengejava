package com.example.challenge.repository;

import com.example.challenge.domain.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório JPA para a entidade Peca.
 * Herda métodos CRUD básicos do JpaRepository.
 */
@Repository
public interface PecaRepository extends JpaRepository<Peca, Long> {
    
    /**
     * Busca uma peça pelo seu código de fabricante.
     * @param codigoFabricante O código SKU/fabricante da peça.
     * @return Um Optional contendo a peça, se encontrada.
     */
    Optional<Peca> findByCodigoFabricante(String codigoFabricante);
    
    // Adicione outros métodos de busca específicos se necessário, 
    // como buscar por localização ou por nome.
}
