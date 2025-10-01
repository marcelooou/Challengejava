package com.example.challenge.service;

import com.example.challenge.domain.Moto;
import com.example.challenge.repository.MotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço que encapsula a lógica de negócios para a entidade Moto.
 * Garante que chassi e placa sejam únicos antes de salvar.
 */
@Service
public class MotoService {

    private final MotoRepository motoRepository;

    public MotoService(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    /**
     * Cria ou atualiza uma moto.
     * @param moto A entidade Moto a ser salva.
     * @return A moto salva.
     */
    @Transactional
    public Moto save(Moto moto) {
        // Regra de Negócio: Checar unicidade de Chassi e Placa
        if (moto.getId() == null) {
            // Checa Chassi na criação
            motoRepository.findByChassi(moto.getChassi()).ifPresent(m -> {
                throw new IllegalStateException("Já existe uma moto cadastrada com o CHASSI: " + m.getChassi());
            });
            // Checa Placa na criação
            motoRepository.findByPlaca(moto.getPlaca()).ifPresent(m -> {
                throw new IllegalStateException("Já existe uma moto cadastrada com a PLACA: " + m.getPlaca());
            });
        } else {
            // Checa unicidade de Chassi e Placa na atualização, ignorando a própria moto
            motoRepository.findByChassi(moto.getChassi())
                    .filter(m -> !m.getId().equals(moto.getId()))
                    .ifPresent(m -> {
                        throw new IllegalStateException("O CHASSI já está em uso por outra moto.");
                    });
            motoRepository.findByPlaca(moto.getPlaca())
                    .filter(m -> !m.getId().equals(moto.getId()))
                    .ifPresent(m -> {
                        throw new IllegalStateException("A PLACA já está em uso por outra moto.");
                    });
        }

        // Regra de Negócio: KM atual não pode ser negativo
        if (moto.getKmAtual() != null && moto.getKmAtual() < 0) {
             throw new IllegalArgumentException("O quilômetro atual não pode ser negativo.");
        }
        
        return motoRepository.save(moto);
    }

    /**
     * Busca todas as motos.
     */
    public List<Moto> findAll() {
        return motoRepository.findAll();
    }

    /**
     * Busca uma moto por ID.
     */
    public Optional<Moto> findById(Long id) {
        return motoRepository.findById(id);
    }

    /**
     * Deleta uma moto por ID.
     */
    @Transactional
    public void delete(Long id) {
        if (!motoRepository.existsById(id)) {
            throw new IllegalArgumentException("Moto com ID " + id + " não encontrada para deleção.");
        }
        motoRepository.deleteById(id);
    }

    /**
     * Busca motos por status.
     */
    public List<Moto> findByStatus(String status) {
        return motoRepository.findByStatus(status);
    }
}
