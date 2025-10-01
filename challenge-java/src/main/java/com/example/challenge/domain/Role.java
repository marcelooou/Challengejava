package com.example.challenge.domain;

// Enum que define os papéis (roles) disponíveis para os usuários no sistema.
// O Spring Security espera que os papéis sejam prefixados com "ROLE_".
public enum Role {
    ROLE_USER,
    ROLE_ADMIN
}
