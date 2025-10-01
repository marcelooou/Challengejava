package com.example.challenge.controller;

import com.example.challenge.domain.Moto;
import com.example.challenge.service.MotoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller responsável por gerenciar a frota de Motos.
 * Mapeia as operações CRUD para as views Thymeleaf.
 */
@Controller
@RequestMapping("/motos")
public class MotoController {

    private final MotoService motoService;

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    /**
     * Exibe a lista de todas as motos no sistema.
     */
    @GetMapping
    public String listMotos(Model model) {
        List<Moto> motos = motoService.findAll();
        model.addAttribute("motos", motos);
        // Retorna para a view de listagem
        return "motos-list";
    }

    /**
     * Exibe o formulário de criação de uma nova moto.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Adiciona um objeto Moto vazio para o formulário
        model.addAttribute("moto", new Moto());
        // Define o título da página
        model.addAttribute("pageTitle", "Cadastrar Nova Moto");
        return "moto-form";
    }

    /**
     * Exibe o formulário de edição de uma moto existente.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        return motoService.findById(id)
                .map(moto -> {
                    model.addAttribute("moto", moto);
                    model.addAttribute("pageTitle", "Editar Moto (ID: " + id + ")");
                    return "moto-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Moto não encontrada para edição.");
                    return "redirect:/motos";
                });
    }

    /**
     * Processa a submissão do formulário de criação ou atualização.
     */
    @PostMapping
    public String saveOrUpdateMoto(@Valid @ModelAttribute("moto") Moto moto,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        
        // Se houver erros de validação (ex: placa vazia), retorna para o formulário
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", (moto.getId() == null ? "Cadastrar Nova Moto" : "Editar Moto (ID: " + moto.getId() + ")"));
            return "moto-form";
        }

        Moto savedMoto = motoService.save(moto);
        
        // Mensagem de sucesso
        redirectAttributes.addFlashAttribute("successMessage", 
                "Moto " + savedMoto.getPlaca() + " salva com sucesso!");

        // Redireciona para a lista
        return "redirect:/motos";
    }

    /**
     * Processa a requisição de deleção de uma moto.
     */
    @GetMapping("/delete/{id}")
    public String deleteMoto(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            motoService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Moto excluída com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/motos";
    }
}
