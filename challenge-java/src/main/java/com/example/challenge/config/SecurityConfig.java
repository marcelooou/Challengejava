package com.example.challenge.config;

import com.example.challenge.domain.User;
import com.example.challenge.repository.UserRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User u = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            
            return org.springframework.security.core.userdetails.User.withUsername(u.getEmail())
                    .password(u.getPasswordHash())
                    .authorities(u.getRole()) 
                    .build();
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // THIS IS THE CRITICAL LINE: It allows access to folders like /css, /js, etc.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers("/login", "/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/pecas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/pecas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/pecas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/pecas/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll())
            .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}