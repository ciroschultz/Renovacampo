package org.acabativa.rc.patrimonio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Permitir origens (adicione seu dominio de producao aqui)
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:5500",
            "http://127.0.0.1:5500",
            "http://localhost:8080",
            "http://sistema.renovacampo.com.br",
            "https://sistema.renovacampo.com.br",
            "http://renovacampo.com.br",
            "https://renovacampo.com.br",
            "https://www.renovacampo.com.br",
            "*"  // Permitir todas as origens temporariamente para desenvolvimento
        ));

        // Permitir todos os metodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Permitir todos os headers
        config.setAllowedHeaders(Arrays.asList("*"));

        // Permitir credenciais (cookies, auth headers)
        config.setAllowCredentials(false); // Definir como false quando usar "*" em origins

        // Tempo de cache do preflight
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
