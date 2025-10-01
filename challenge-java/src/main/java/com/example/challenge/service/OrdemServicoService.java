package com.example.challenge.service;

import com.example.challenge.domain.Moto;
import com.example.challenge.domain.OrdemServico;
import com.example.challenge.repository.MotoRepository;
import com.example.challenge.repository.OrdemServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço que encapsula a lógica de negócios para a entidade OrdemServico.
 * Garante que a moto referenciada exista antes de salvar a ordem.
 */
@Service
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final MotoRepository motoRepository; // Injetamos MotoRepository para validar a existência da moto

    public OrdemServicoService(OrdemServicoRepository ordemServicoRepository, MotoRepository motoRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.motoRepository = motoRepository;
    }

    /**
     * Cria ou atualiza uma ordem de serviço.
     * @param ordemServico A entidade OrdemServico a ser salva.
     * @return A ordem de serviço salva.
     */
    @Transactional
    public OrdemServico save(OrdemServico ordemServico) {
        // Regra de Negócio Crítica: A moto referenciada DEVE existir.
        if (ordemServico.getMoto() == null || ordemServico.getMoto().getId() == null) {
            throw new IllegalArgumentException("A referência à Moto é obrigatória.");
        }

        Long motoId = ordemServico.getMoto().getId();
        Optional<Moto> motoOpt = motoRepository.findById(motoId);

        if (motoOpt.isEmpty()) {
            throw new IllegalStateException("A Moto com ID " + motoId + " não foi encontrada. Não é possível criar a Ordem de Serviço.");
        }
        
        // Atribui a entidade Moto gerenciada ao relacionamento, se for uma nova ordem
        if (ordemServico.getId() == null) {
            ordemServico.setMoto(motoOpt.get());
        }

        return ordemServicoRepository.save(ordemServico);
    }

    /**
     * Busca todas as ordens de serviço.
     */
    public List<OrdemServico> findAll() {
        return ordemServicoRepository.findAll();
    }

    /**
     * Busca uma ordem de serviço por ID.
     */
    public Optional<OrdemServico> findById(Long id) {
        return ordemServicoRepository.findById(id);
    }

    /**
     * Deleta uma ordem de serviço por ID.
     */
    @Transactional
    public void delete(Long id) {
        if (!ordemServicoRepository.existsById(id)) {
            throw new IllegalArgumentException("Ordem de Serviço com ID " + id + " não encontrada para deleção.");
        }
        ordemServicoRepository.deleteById(id);
    }

    /**
     * Busca ordens por status.
     */
    public List<OrdemServico> findByStatus(String status) {
        return ordemServicoRepository.findByStatus(status);
    }
}
