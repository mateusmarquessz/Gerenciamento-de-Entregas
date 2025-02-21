package com.frete.mais.gerenciamento_de_entregas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Somente ADMIN pode criar novos admins
                        .requestMatchers(HttpMethod.POST, "/auth/register-admin").hasRole("ADMIN")

                        // Permissões para entregas
                        .requestMatchers(HttpMethod.GET, "/entregas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/entregas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/entregas/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/entregas/{id}").hasRole("ADMIN")

                        // USER pode listar e atualizar suas próprias entregas
                        .requestMatchers(HttpMethod.GET, "/entregas/usuario/{usuarioId}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/entregas/{entregaId}/atualizar-status").hasRole("USER")

                        // Permissões do Usuario
                        .requestMatchers(HttpMethod.GET, "/api/users/profile/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")

                        // Qualquer outra requisição exige autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
