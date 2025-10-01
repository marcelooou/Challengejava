package com.example.challenge.controller;

import com.example.challenge.domain.Peca;
import com.example.challenge.dto.EstoqueUpdateDTO;
import com.example.challenge.dto.PecaDTO;
import com.example.challenge.service.PecaService;
import com.example.challenge.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PecaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PecaService pecaService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private Peca peca;
    private PecaDTO pecaDTO;
    private EstoqueUpdateDTO estoqueUpdateDTO;

    @BeforeEach
    public void setup() {
        peca = new Peca();
        peca.setId(1L);
        peca.setNome("Filtro de Óleo");
        peca.setDescricao("Filtro de óleo para motor");
        peca.setPreco(50.0);
        peca.setCodigoFabricante("FO-M001");
        peca.setEstoqueAtual(50);
        peca.setEstoqueMinimo(10);
        peca.setLocalizacaoEstoque("Prateleira B3");

        pecaDTO = new PecaDTO();
        pecaDTO.setNome("Filtro de Óleo");
        pecaDTO.setCodigoFabricante("FO-M001");
        pecaDTO.setEstoqueAtual(50);
        pecaDTO.setEstoqueMinimo(10);
        pecaDTO.setLocalizacaoEstoque("Prateleira B3");
        
        // CORREÇÃO FINAL: Adicionando os campos que faltavam para passar na validação
        pecaDTO.setDescricao("Filtro de óleo para motor");
        pecaDTO.setPreco(50.0);

        estoqueUpdateDTO = new EstoqueUpdateDTO();
        estoqueUpdateDTO.setQuantidade(10);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void createPeca_DeveRetornar201Created_QuandoValido() throws Exception {
        when(pecaService.save(any(Peca.class))).thenReturn(peca);

        mockMvc.perform(post("/api/pecas").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pecaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.codigoFabricante").value("FO-M001"));

        verify(pecaService, times(1)).save(any(Peca.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void createPeca_DeveRetornar409Conflict_QuandoCodigoDuplicado() throws Exception {
        when(pecaService.save(any(Peca.class))).thenThrow(IllegalStateException.class);

        mockMvc.perform(post("/api/pecas").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pecaDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void createPeca_DeveRetornar403Forbidden_ParaNaoAdmin() throws Exception {
        mockMvc.perform(post("/api/pecas").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pecaDTO)))
                .andExpect(status().isForbidden());

        verify(pecaService, never()).save(any(Peca.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void updatePeca_DeveRetornar200Ok_QuandoEncontrado() throws Exception {
        when(pecaService.findById(1L)).thenReturn(Optional.of(peca));
        when(pecaService.save(any(Peca.class))).thenReturn(peca);

        mockMvc.perform(put("/api/pecas/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pecaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(pecaService, times(1)).save(any(Peca.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void updatePeca_DeveRetornar404NotFound_QuandoNaoEncontrado() throws Exception {
        when(pecaService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/pecas/99").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pecaDTO)))
                .andExpect(status().isNotFound());

        verify(pecaService, never()).save(any(Peca.class));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void getAllPecas_DeveRetornar200Ok_ParaUsuarioComum() throws Exception {
        List<Peca> pecas = Arrays.asList(peca, new Peca());
        when(pecaService.findAll()).thenReturn(pecas);

        mockMvc.perform(get("/api/pecas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(pecaService, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void getAllPecas_DeveRetornar200Ok_ParaAdmin() throws Exception {
        List<Peca> pecas = Arrays.asList(peca);
        when(pecaService.findAll()).thenReturn(pecas);

        mockMvc.perform(get("/api/pecas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void getPecaById_DeveRetornar200Ok_QuandoEncontrado() throws Exception {
        when(pecaService.findById(1L)).thenReturn(Optional.of(peca));

        mockMvc.perform(get("/api/pecas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Filtro de Óleo"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void getPecaById_DeveRetornar404NotFound_QuandoNaoEncontrado() throws Exception {
        when(pecaService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pecas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void deletePeca_DeveRetornar204NoContent_QuandoSucesso() throws Exception {
        doNothing().when(pecaService).delete(1L);

        mockMvc.perform(delete("/api/pecas/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(pecaService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void deletePeca_DeveRetornar404NotFound_QuandoNaoEncontrado() throws Exception {
        doThrow(new IllegalArgumentException()).when(pecaService).delete(99L);

        mockMvc.perform(delete("/api/pecas/99").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void deletePeca_DeveRetornar403Forbidden_ParaNaoAdmin() throws Exception {
        mockMvc.perform(delete("/api/pecas/1").with(csrf()))
                .andExpect(status().isForbidden());

        verify(pecaService, never()).delete(anyLong());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void updateEstoque_DeveRetornar200Ok_ComSucesso() throws Exception {
        Peca pecaAtualizada = new Peca();
        pecaAtualizada.setEstoqueAtual(60);

        when(pecaService.updateEstoque("FO-M001", 10)).thenReturn(pecaAtualizada);

        mockMvc.perform(patch("/api/pecas/estoque/FO-M001").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estoqueUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estoqueAtual").value(60));

        verify(pecaService, times(1)).updateEstoque("FO-M001", 10);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void updateEstoque_DeveRetornar400BadRequest_QuandoEstoqueInvalido() throws Exception {
        when(pecaService.updateEstoque(anyString(), anyInt())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(patch("/api/pecas/estoque/FO-M001").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estoqueUpdateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void updateEstoque_DeveRetornar403Forbidden_ParaNaoAdmin() throws Exception {
        mockMvc.perform(patch("/api/pecas/estoque/FO-M001").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estoqueUpdateDTO)))
                .andExpect(status().isForbidden());

        verify(pecaService, never()).updateEstoque(anyString(), anyInt());
    }
}