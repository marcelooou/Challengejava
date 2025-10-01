package com.example.challenge.controller;

import com.example.challenge.domain.Peca;
import com.example.challenge.dto.EstoqueUpdateDTO;
import com.example.challenge.dto.PecaDTO;
import com.example.challenge.service.PecaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaService pecaService;

    public PecaController(PecaService pecaService) {
        this.pecaService = pecaService;
    }

    @GetMapping
    public ResponseEntity<List<Peca>> getAllPecas() {
        List<Peca> pecas = pecaService.findAll();
        return ResponseEntity.ok(pecas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peca> getPecaById(@PathVariable Long id) {
        return pecaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Peca> createPeca(@Valid @RequestBody PecaDTO pecaDTO) {
        try {
            Peca pecaToSave = new Peca();
            pecaToSave.setNome(pecaDTO.getNome());
            pecaToSave.setDescricao(pecaDTO.getDescricao()); // Campo adicionado
            pecaToSave.setCodigoFabricante(pecaDTO.getCodigoFabricante());
            pecaToSave.setPreco(pecaDTO.getPreco()); // Campo adicionado
            pecaToSave.setEstoqueMinimo(pecaDTO.getEstoqueMinimo());
            pecaToSave.setEstoqueAtual(pecaDTO.getEstoqueAtual());
            pecaToSave.setLocalizacaoEstoque(pecaDTO.getLocalizacaoEstoque());

            Peca savedPeca = pecaService.save(pecaToSave);
            return ResponseEntity.created(URI.create("/api/pecas/" + savedPeca.getId())).body(savedPeca);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Peca> updatePeca(@PathVariable Long id, @Valid @RequestBody PecaDTO pecaDTO) {
        return pecaService.findById(id)
                .map(pecaExistente -> {
                    pecaExistente.setNome(pecaDTO.getNome());
                    pecaExistente.setDescricao(pecaDTO.getDescricao()); // Campo adicionado
                    pecaExistente.setCodigoFabricante(pecaDTO.getCodigoFabricante());
                    pecaExistente.setPreco(pecaDTO.getPreco()); // Campo adicionado
                    pecaExistente.setEstoqueMinimo(pecaDTO.getEstoqueMinimo());
                    pecaExistente.setEstoqueAtual(pecaDTO.getEstoqueAtual());
                    pecaExistente.setLocalizacaoEstoque(pecaDTO.getLocalizacaoEstoque());
                    Peca updatedPeca = pecaService.save(pecaExistente);
                    return ResponseEntity.ok(updatedPeca);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeca(@PathVariable Long id) {
        try {
            pecaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/estoque/{codigoFabricante}")
    public ResponseEntity<Peca> updateEstoque(@PathVariable String codigoFabricante, @Valid @RequestBody EstoqueUpdateDTO estoqueUpdateDTO) {
        try {
            Peca pecaAtualizada = pecaService.updateEstoque(codigoFabricante, estoqueUpdateDTO.getQuantidade());
            return ResponseEntity.ok(pecaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}