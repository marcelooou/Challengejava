package com.example.challenge.controller;

import com.example.challenge.domain.OrdemServico;
import com.example.challenge.domain.Moto;
import com.example.challenge.domain.Peca;
import com.example.challenge.service.OrdemServicoService;
import com.example.challenge.service.MotoService;
import com.example.challenge.service.PecaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller responsável por gerenciar as Ordens de Serviço (Manutenção).
 * Mapeia as operações CRUD para as views Thymeleaf.
 */
@Controller
@RequestMapping("/ordens")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;
    private final MotoService motoService;
    private final PecaService pecaService;

    public OrdemServicoController(OrdemServicoService ordemServicoService, MotoService motoService, PecaService pecaService) {
        this.ordemServicoService = ordemServicoService;
        this.motoService = motoService;
        this.pecaService = pecaService;
    }

    // --- Métodos de Listagem e Exibição de Formulário ---

    /**
     * Exibe a lista de todas as Ordens de Serviço.
     */
    @GetMapping
    public String listOrdens(Model model) {
        List<OrdemServico> ordens = ordemServicoService.findAll();
        model.addAttribute("ordens", ordens);
        return "ordens-list";
    }

    /**
     * Prepara o Model com listas de motos e peças para o formulário.
     */
    private void loadFormDependencies(Model model) {
        // Carrega todas as motos para o dropdown de seleção
        List<Moto> motos = motoService.findAll();
        // Carrega todas as peças para a seleção de itens usados
        List<Peca> pecas = pecaService.findAll();
        
        model.addAttribute("motos", motos);
        model.addAttribute("pecas", pecas);
    }

    /**
     * Exibe o formulário de criação de uma nova Ordem de Serviço.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ordemServico", new OrdemServico());
        model.addAttribute("pageTitle", "Abrir Nova Ordem de Serviço");
        loadFormDependencies(model);
        return "ordem-form";
    }

    /**
     * Exibe o formulário de edição de uma Ordem de Serviço existente.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        return ordemServicoService.findById(id)
                .map(ordemServico -> {
                    model.addAttribute("ordemServico", ordemServico);
                    model.addAttribute("pageTitle", "Editar Ordem de Serviço (ID: " + id + ")");
                    loadFormDependencies(model);
                    return "ordem-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Ordem de Serviço não encontrada para edição.");
                    return "redirect:/ordens";
                });
    }

    // --- Métodos de Processamento ---

    /**
     * Processa a submissão do formulário de criação ou atualização.
     */
    @PostMapping
    public String saveOrUpdateOrdem(@Valid @ModelAttribute("ordemServico") OrdemServico ordemServico,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        
        // Verifica se o ID da moto foi fornecido no formulário
        if (ordemServico.getMoto() == null || ordemServico.getMoto().getId() == null) {
            result.rejectValue("moto", "moto.required", "A moto é obrigatória.");
        }

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", (ordemServico.getId() == null ? "Abrir Nova Ordem de Serviço" : "Editar Ordem de Serviço (ID: " + ordemServico.getId() + ")"));
            loadFormDependencies(model); // Recarrega as dependências em caso de erro
            return "ordem-form";
        }

        // Se a validação do Thymeleaf for OK, buscamos as entidades para salvar
        Moto moto = motoService.findById(ordemServico.getMoto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Moto não encontrada."));
        ordemServico.setMoto(moto);

        // O Service cuidará de atualizar o KM da moto e dar baixa no estoque
        OrdemServico savedOrdem = ordemServicoService.save(ordemServico);
        
        redirectAttributes.addFlashAttribute("successMessage", 
                "Ordem de Serviço #" + savedOrdem.getId() + " para a moto " + savedOrdem.getMoto().getPlaca() + " salva com sucesso!");

        return "redirect:/ordens";
    }

    /**
     * Processa a requisição de deleção de uma Ordem de Serviço.
     */
    @GetMapping("/delete/{id}")
    public String deleteOrdem(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            ordemServicoService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ordem de Serviço excluída com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/ordens";
    }
}
