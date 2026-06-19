package com.example.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http) {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers("/auth/**").permitAll()

                        .pathMatchers("/clientes/**").permitAll()

                        .pathMatchers("/compras/**").permitAll()

                        .pathMatchers("/productos/**").permitAll()

                        .pathMatchers("/inventarios/**").permitAll()

                        .pathMatchers("/pagos/**").permitAll()

                        .pathMatchers("/categorias/**").permitAll()

                        .pathMatchers("/resenas/**").permitAll()

                        .anyExchange().authenticated()
                )

                .httpBasic(httpBasic -> httpBasic.disable())

                .formLogin(form -> form.disable());

        return http.build();
    }
}