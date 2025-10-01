package com.example.challenge.service;

import com.example.challenge.domain.Peca;
import com.example.challenge.repository.PecaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes Unitários para a classe PecaService.
 * Testa as operações CRUD e as regras de negócio:
 * 1. Unicidade do codigoFabricante.
 * 2. Estoque não pode ser negativo.
 * 3. Validação de deleção.
 */
@ExtendWith(MockitoExtension.class)
public class PecaServiceTest {

    // Simula a dependência do banco de dados (Repository)
    @Mock
    private PecaRepository pecaRepository;

    // Injeta as dependências no PecaService
    @InjectMocks
    private PecaService pecaService;

    private Peca pecaValida;
    private Peca pecaExistenteComCodigo;

    // O método setUp() que estava falhando na compilação.
    // É crucial que ele tenha a assinatura "public void setUp()"
    @BeforeEach
    public void setUp() {
        // Objeto Peca para testes de criação
        pecaValida = new Peca();
        pecaValida.setId(null);
        pecaValida.setNome("Pneu Dianteiro");
        pecaValida.setCodigoFabricante("PD-001");
        pecaValida.setEstoqueAtual(10);
        pecaValida.setEstoqueMinimo(5);

        // Objeto Peca para simular uma peça já existente no repositório
        pecaExistenteComCodigo = new Peca();
        pecaExistenteComCodigo.setId(2L);
        pecaExistenteComCodigo.setCodigoFabricante("PD-001");
    }

    // --- Testes para SAVE (Criação/Atualização) ---

    @Test
    public void save_DeveSalvarNovaPeca_QuandoCodigoNaoExiste() {
        // Dado: O código do fabricante não existe no repositório
        when(pecaRepository.findByCodigoFabricante(pecaValida.getCodigoFabricante())).thenReturn(Optional.empty());
        when(pecaRepository.save(any(Peca.class))).thenReturn(pecaValida);

        // Quando: A peça é salva
        Peca result = pecaService.save(pecaValida);

        // Então: O save deve ser chamado e o resultado não deve ser nulo
        assertNotNull(result);
        verify(pecaRepository, times(1)).save(pecaValida);
    }

    @Test
    public void save_DeveLancarExcecao_QuandoCodigoFabricanteJaExiste() {
        // Dado: A peça é nova (id=null) e o código do fabricante já existe
        when(pecaRepository.findByCodigoFabricante(pecaValida.getCodigoFabricante())).thenReturn(Optional.of(pecaExistenteComCodigo));

        // Quando: Tentamos salvar
        // Então: Deve lançar IllegalStateException (regra de negócio de unicidade)
        assertThrows(IllegalStateException.class, () -> pecaService.save(pecaValida));
        verify(pecaRepository, never()).save(any(Peca.class));
    }

    @Test
    public void save_DeveLancarExcecao_QuandoEstoqueInicialNegativo() {
        // Dado: Estoque atual inválido
        pecaValida.setEstoqueAtual(-5);

        // Quando: Tentamos salvar
        // Então: Deve lançar IllegalArgumentException (regra de negócio de estoque)
        assertThrows(IllegalArgumentException.class, () -> pecaService.save(pecaValida));
        verify(pecaRepository, never()).save(any(Peca.class));
    }

    // --- Testes para FINDALL ---

    @Test
    public void findAll_DeveRetornarListaDePecas() {
        // Dado: Duas peças no repositório
        List<Peca> pecas = Arrays.asList(pecaValida, pecaExistenteComCodigo);
        when(pecaRepository.findAll()).thenReturn(pecas);

        // Quando: Buscamos todas as peças
        List<Peca> result = pecaService.findAll();

        // Então: Deve retornar a lista com 2 itens
        assertEquals(2, result.size());
        verify(pecaRepository, times(1)).findAll();
    }

    // --- Testes para FINDBYID ---

    @Test
    public void findById_DeveRetornarPeca_QuandoEncontrada() {
        // Dado: ID 1 existe
        when(pecaRepository.findById(1L)).thenReturn(Optional.of(pecaValida));

        // Quando: Buscamos pelo ID 1
        Optional<Peca> result = pecaService.findById(1L);

        // Então: O resultado deve estar presente
        assertTrue(result.isPresent());
        assertEquals("PD-001", result.get().getCodigoFabricante());
    }

    @Test
    public void findById_DeveRetornarOptionalVazio_QuandoNaoEncontrada() {
        // Dado: ID 99 não existe
        when(pecaRepository.findById(99L)).thenReturn(Optional.empty());

        // Quando: Buscamos pelo ID 99
        Optional<Peca> result = pecaService.findById(99L);

        // Então: O resultado deve ser Optional vazio
        assertTrue(result.isEmpty());
    }

    // --- Testes para DELETE ---

    @Test
    public void delete_DeveDeletarPeca_QuandoEncontrada() {
        // Dado: ID 1 existe
        when(pecaRepository.existsById(1L)).thenReturn(true);

        // Quando: Deletamos a peça
        assertDoesNotThrow(() -> pecaService.delete(1L));

        // Então: O método deleteById deve ser chamado
        verify(pecaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void delete_DeveLancarExcecao_QuandoNaoEncontrada() {
        // Dado: ID 99 não existe
        when(pecaRepository.existsById(99L)).thenReturn(false);

        // Quando: Tentamos deletar
        // Então: Deve lançar IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> pecaService.delete(99L));
        verify(pecaRepository, never()).deleteById(anyLong());
    }
    
    // --- Testes para UPDATEESTOQUE ---

    @Test
    public void updateEstoque_DeveAumentarEstoque_ComSucesso() {
        // Dado: Estoque atual é 10. Queremos adicionar 5.
        pecaValida.setId(1L);
        pecaValida.setEstoqueAtual(10);
        when(pecaRepository.findByCodigoFabricante("PD-001")).thenReturn(Optional.of(pecaValida));
        
        Peca pecaEsperada = new Peca();
        pecaEsperada.setId(1L);
        pecaEsperada.setEstoqueAtual(15);
        when(pecaRepository.save(any(Peca.class))).thenReturn(pecaEsperada);

        // Quando: Atualizamos o estoque
        Peca result = pecaService.updateEstoque("PD-001", 5);

        // Então: O novo estoque deve ser 15
        assertEquals(15, result.getEstoqueAtual());
        verify(pecaRepository, times(1)).save(pecaValida);
    }
    
    @Test
    public void updateEstoque_DeveDiminuirEstoque_ComSucesso() {
        // Dado: Estoque atual é 10. Queremos remover 8.
        pecaValida.setId(1L);
        pecaValida.setEstoqueAtual(10);
        when(pecaRepository.findByCodigoFabricante("PD-001")).thenReturn(Optional.of(pecaValida));
        
        Peca pecaEsperada = new Peca();
        pecaEsperada.setId(1L);
        pecaEsperada.setEstoqueAtual(2);
        when(pecaRepository.save(any(Peca.class))).thenReturn(pecaEsperada);

        // Quando: Atualizamos o estoque
        Peca result = pecaService.updateEstoque("PD-001", -8);

        // Então: O novo estoque deve ser 2
        assertEquals(2, result.getEstoqueAtual());
        verify(pecaRepository, times(1)).save(pecaValida);
    }

    @Test
    public void updateEstoque_DeveLancarExcecao_SeResultadoForNegativo() {
        // Dado: Estoque atual é 10. Queremos remover 15 (resultando em -5).
        pecaValida.setId(1L);
        pecaValida.setEstoqueAtual(10);
        when(pecaRepository.findByCodigoFabricante("PD-001")).thenReturn(Optional.of(pecaValida));

        // Quando: Tentamos atualizar
        // Então: Deve lançar IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> pecaService.updateEstoque("PD-001", -15));
        verify(pecaRepository, never()).save(any(Peca.class));
    }
    
    @Test
    public void updateEstoque_DeveLancarExcecao_SePecaNaoForEncontrada() {
        // Dado: A peça com o código não existe
        when(pecaRepository.findByCodigoFabricante("COD-INV")).thenReturn(Optional.empty());

        // Quando: Tentamos atualizar
        // Então: Deve lançar IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> pecaService.updateEstoque("COD-INV", 10));
        verify(pecaRepository, never()).save(any(Peca.class));
    }
}
