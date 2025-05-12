package com.encontreaqui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Habilita CORS para que a configuração global em CorsConfig seja aplicada
            .cors().and()
            // Desabilita CSRF (útil para APIs REST)
            .csrf(csrf -> csrf.disable())
            // Define que a sessão será stateless (JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas: endpoints de login, registro, uploads e apenas o GET de /api/comercios
                .requestMatchers("/api/usuarios/login", "/api/usuarios/registro", "/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/comercios").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/comercios/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/servicos").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/servicos/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/servicos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/alugueis").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/alugueis/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/avaliacoes").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/avaliacoes/item").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/avaliacoes/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/avaliacoes/responder").permitAll()
                // Outras requisições precisam de autenticação
                .anyRequest().authenticated()
            );

        // Adiciona o filtro JWT antes do filtro padrão de autenticação
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Expondo o AuthenticationManager se necessário
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
