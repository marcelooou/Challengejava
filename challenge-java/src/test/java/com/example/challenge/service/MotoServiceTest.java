package com.example.challenge.service;

import com.example.challenge.domain.Moto;
import com.example.challenge.repository.MotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes Unitários para a classe MotoService.
 * Cobre as operações CRUD da entidade principal (Moto) e busca por status.
 */
@ExtendWith(MockitoExtension.class)
class MotoServiceTest {

    @Mock
    private MotoRepository motoRepository;

    @InjectMocks
    private MotoService motoService;

    private Moto motoExistente;

    @BeforeEach
    void setUp() {
        // Inicializa uma Moto de referência para os testes
        motoExistente = new Moto();
        motoExistente.setId(1L);
        motoExistente.setPlaca("ABC1234");
        motoExistente.setModelo("YBR 150");
        motoExistente.setStatus("Disponível");
        motoExistente.setDataCadastro(LocalDateTime.now());
        motoExistente.setKmAtual(15000);
    }

    // ====================================================================
    // TESTES DE CRIAÇÃO E ATUALIZAÇÃO (SAVE)
    // ====================================================================

    @Test
    void save_Success() {
        // Simula que o repositório salva e retorna a mesma moto com o ID
        when(motoRepository.save(any(Moto.class))).thenReturn(motoExistente);

        Moto result = motoService.save(motoExistente);

        assertNotNull(result);
        assertEquals("ABC1234", result.getPlaca());
        verify(motoRepository, times(1)).save(motoExistente);
    }

    // ====================================================================
    // TESTES DE BUSCA (FIND)
    // ====================================================================

    @Test
    void findById_Success() {
        when(motoRepository.findById(1L)).thenReturn(Optional.of(motoExistente));

        Optional<Moto> result = motoService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("YBR 150", result.get().getModelo());
        verify(motoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(motoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Moto> result = motoService.findById(99L);

        assertFalse(result.isPresent());
        verify(motoRepository, times(1)).findById(99L);
    }

    @Test
    void findAll_Success() {
        Moto moto2 = new Moto();
        moto2.setId(2L);
        moto2.setPlaca("XYZ9876");

        List<Moto> motos = Arrays.asList(motoExistente, moto2);
        when(motoRepository.findAll()).thenReturn(motos);

        List<Moto> result = motoService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(motoRepository, times(1)).findAll();
    }

    @Test
    void findByStatus_Success() {
        Moto motoManutencao = new Moto();
        motoManutencao.setId(3L);
        motoManutencao.setStatus("Manutenção");
        motoManutencao.setPlaca("DEF5678");

        List<Moto> motosManutencao = List.of(motoManutencao);
        when(motoRepository.findByStatus("Manutenção")).thenReturn(motosManutencao);

        List<Moto> result = motoService.findByStatus("Manutenção");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Manutenção", result.get(0).getStatus());
        verify(motoRepository, times(1)).findByStatus("Manutenção");
    }

    // ====================================================================
    // TESTES DE DELEÇÃO (DELETE)
    // ====================================================================

    @Test
    void delete_Success_WhenIdExists() {
        when(motoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(motoRepository).deleteById(1L);

        // Verifica se o método não lança exceção
        assertDoesNotThrow(() -> motoService.delete(1L));

        verify(motoRepository, times(1)).existsById(1L);
        verify(motoRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_Failure_WhenIdDoesNotExist() {
        when(motoRepository.existsById(99L)).thenReturn(false);

        // Verifica se o método lança a exceção esperada
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            motoService.delete(99L);
        });

        // Garante a mensagem de erro correta
        assertEquals("Moto com ID 99 não encontrada para deleção.", exception.getMessage());

        verify(motoRepository, times(1)).existsById(99L);
        verify(motoRepository, never()).deleteById(anyLong());
    }
}