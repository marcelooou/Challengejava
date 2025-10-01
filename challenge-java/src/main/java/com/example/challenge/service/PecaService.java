package com.example.challenge.service;

import com.example.challenge.domain.Peca;
import com.example.challenge.repository.PecaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço que encapsula a lógica de negócios para a entidade Peca.
 * Gerencia operações CRUD e garante regras de negócio (como checagem de estoque).
 */
@Service
public class PecaService {

    private final PecaRepository pecaRepository;

    public PecaService(PecaRepository pecaRepository) {
        this.pecaRepository = pecaRepository;
    }

    /**
     * Cria ou atualiza uma peça.
     * @param peca A entidade Peca a ser salva.
     * @return A peça salva.
     */
    @Transactional
    public Peca save(Peca peca) {
        // Regra de negócio: Se for uma nova peça, checa se o código de fabricante já existe
        if (peca.getId() == null) {
            pecaRepository.findByCodigoFabricante(peca.getCodigoFabricante()).ifPresent(p -> {
                throw new IllegalStateException("Já existe uma peça com o código de fabricante: " + p.getCodigoFabricante());
            });
        }
        
        // Regra de negócio: Estoque não pode ser negativo
        if (peca.getEstoqueAtual() < 0) {
            throw new IllegalArgumentException("O estoque atual não pode ser negativo.");
        }
        
        return pecaRepository.save(peca);
    }

    /**
     * Busca todas as peças.
     */
    public List<Peca> findAll() {
        return pecaRepository.findAll();
    }

    /**
     * Busca uma peça por ID.
     */
    public Optional<Peca> findById(Long id) {
        return pecaRepository.findById(id);
    }
    
    /**
     * Deleta uma peça por ID.
     * @param id O ID da peça a ser deletada.
     */
    @Transactional
    public void delete(Long id) {
        if (!pecaRepository.existsById(id)) {
            throw new IllegalArgumentException("Peça com ID " + id + " não encontrada para deleção.");
        }
        pecaRepository.deleteById(id);
    }
    
    /**
     * Atualiza o estoque de uma peça.
     * O PecaController passa a quantidade como Integer (objeto), mas aqui
     * o método usa o tipo primitivo 'int' para o cálculo, o que é mais eficiente
     * e compatível com a chamada do Controller (o Java lida com o autoboxing).
     * * @param codigoFabricante O código da peça.
     * @param quantidade Alteração no estoque (positivo para entrada, negativo para saída).
     * @return A peça atualizada.
     */
    @Transactional
    public Peca updateEstoque(String codigoFabricante, int quantidade) {
        Peca peca = pecaRepository.findByCodigoFabricante(codigoFabricante)
                                  .orElseThrow(() -> new IllegalArgumentException("Peça com código " + codigoFabricante + " não encontrada."));
        
        int novoEstoque = peca.getEstoqueAtual() + quantidade;
        
        if (novoEstoque < 0) {
            throw new IllegalArgumentException("Operação de estoque resultaria em valor negativo.");
        }
        
        peca.setEstoqueAtual(novoEstoque);
        return pecaRepository.save(peca);
    }
}
