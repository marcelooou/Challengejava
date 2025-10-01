package com.example.challenge.service;

import com.example.challenge.domain.Moto;
import com.example.challenge.domain.OrdemServico;
import com.example.challenge.repository.MotoRepository;
import com.example.challenge.repository.OrdemServicoRepository;
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
 * Testes Unitários para a classe OrdemServicoService.
 * Foca em validar a lógica de negócios, especialmente a verificação de Moto existente.
 */
@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {

    @Mock
    private OrdemServicoRepository ordemServicoRepository;

    @Mock
    private MotoRepository motoRepository;

    @InjectMocks
    private OrdemServicoService ordemServicoService;

    private Moto motoExistente;
    private OrdemServico ordemServico;

    @BeforeEach
    void setUp() {
        // Inicializa a Moto de referência que deve existir
        motoExistente = new Moto();
        motoExistente.setId(1L);
        motoExistente.setModelo("XRE 300");

        // Inicializa a Ordem de Serviço de teste
        ordemServico = new OrdemServico();
        ordemServico.setId(10L);
        ordemServico.setMoto(motoExistente);
        ordemServico.setDescricaoProblema("Troca de pneus e freios.");
        ordemServico.setStatus("Aberta");
        ordemServico.setDataAbertura(LocalDateTime.now());
    }

    // ====================================================================
    // TESTES DE CRIAÇÃO (SAVE)
    // ====================================================================

    @Test
    void save_Success_WhenMotoExists() {
        // Configura o Mock: Simula que a moto é encontrada
        when(motoRepository.findById(1L)).thenReturn(Optional.of(motoExistente));
        // Configura o Mock: Simula que o repositório salva a Ordem
        when(ordemServicoRepository.save(any(OrdemServico.class))).thenReturn(ordemServico);

        OrdemServico result = ordemServicoService.save(ordemServico);

        // Verifica
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(motoExistente, result.getMoto());

        // Garante que o método findById da Moto foi chamado
        verify(motoRepository, times(1)).findById(1L);
        verify(ordemServicoRepository, times(1)).save(ordemServico);
    }

    @Test
    void save_Failure_WhenMotoDoesNotExist() {
        // Configura o Mock: Simula que a moto NÃO é encontrada
        when(motoRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica se o método lança a exceção esperada
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ordemServicoService.save(ordemServico);
        });

        // Garante a mensagem de erro correta
        assertEquals("A Moto com ID 1 não foi encontrada. Não é possível criar a Ordem de Serviço.", exception.getMessage());

        // Garante que o método save() NUNCA foi chamado no repositório
        verify(ordemServicoRepository, never()).save(any(OrdemServico.class));
    }

    @Test
    void save_Failure_WhenMotoIdIsNull() {
        ordemServico.getMoto().setId(null);

        // Verifica se o método lança a exceção esperada
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ordemServicoService.save(ordemServico);
        });

        // Garante a mensagem de erro correta
        assertEquals("A referência à Moto é obrigatória.", exception.getMessage());

        verify(motoRepository, never()).findById(anyLong());
    }


    // ====================================================================
    // TESTES DE BUSCA (FIND)
    // ====================================================================

    @Test
    void findById_Success() {
        when(ordemServicoRepository.findById(10L)).thenReturn(Optional.of(ordemServico));

        Optional<OrdemServico> result = ordemServicoService.findById(10L);

        assertTrue(result.isPresent());
        assertEquals("Aberta", result.get().getStatus());
        verify(ordemServicoRepository, times(1)).findById(10L);
    }

    @Test
    void findAll_Success() {
        OrdemServico os2 = new OrdemServico();
        os2.setId(11L);
        os2.setMoto(motoExistente);
        os2.setStatus("Concluída");

        List<OrdemServico> ordens = Arrays.asList(ordemServico, os2);
        when(ordemServicoRepository.findAll()).thenReturn(ordens);

        List<OrdemServico> result = ordemServicoService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ordemServicoRepository, times(1)).findAll();
    }

    @Test
    void findByStatus_Success() {
        OrdemServico os2 = new OrdemServico();
        os2.setId(11L);
        os2.setMoto(motoExistente);
        os2.setStatus("Concluída");

        List<OrdemServico> ordensConcluidas = List.of(os2);
        when(ordemServicoRepository.findByStatus("Concluída")).thenReturn(ordensConcluidas);

        List<OrdemServico> result = ordemServicoService.findByStatus("Concluída");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Concluída", result.get(0).getStatus());
        verify(ordemServicoRepository, times(1)).findByStatus("Concluída");
    }

    // ====================================================================
    // TESTES DE DELEÇÃO (DELETE)
    // ====================================================================

    @Test
    void delete_Success_WhenIdExists() {
        when(ordemServicoRepository.existsById(10L)).thenReturn(true);
        doNothing().when(ordemServicoRepository).deleteById(10L);

        assertDoesNotThrow(() -> ordemServicoService.delete(10L));

        verify(ordemServicoRepository, times(1)).existsById(10L);
        verify(ordemServicoRepository, times(1)).deleteById(10L);
    }

    @Test
    void delete_Failure_WhenIdDoesNotExist() {
        when(ordemServicoRepository.existsById(99L)).thenReturn(false);

        // Verifica se o método lança a exceção esperada
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ordemServicoService.delete(99L);
        });

        // Garante a mensagem de erro correta
        assertEquals("Ordem de Serviço com ID 99 não encontrada para deleção.", exception.getMessage());

        verify(ordemServicoRepository, times(1)).existsById(99L);
        verify(ordemServicoRepository, never()).deleteById(anyLong());
    }
}