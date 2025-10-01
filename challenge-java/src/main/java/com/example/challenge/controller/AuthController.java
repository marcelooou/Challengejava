package com.example.challenge.controller;

import com.example.challenge.dto.RegisterDTO;
import com.example.challenge.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
                               BindingResult bindingResult,
                               Model model) {

        if (registerDTO.getPassword() != null && !registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "passwords.mismatch", "As senhas não conferem");
        }
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.create(
                registerDTO.getFullName(), 
                registerDTO.getEmail(), 
                registerDTO.getPassword(),
                false
            );
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erro ao realizar o cadastro. E-mail já registrado.");
            return "auth/register";
        }
    }

    // ----- MÉTODO ADICIONADO PARA EXIBIR O DASHBOARD -----
    @GetMapping("/")
    public String showDashboard() {
        // Esta linha diz ao Thymeleaf para renderizar o arquivo 'index.html'
        return "index";
    }
}