package com.example.challenge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para o registro de um novo usuário. 
 * Foi movido para este arquivo para resolver o erro de classe pública.
 */
@Data
public class UserRegistrationDTO {
    
    private Long id; 
    
    @NotBlank(message = "O nome é obrigatório")
    private String name;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String password;
}
