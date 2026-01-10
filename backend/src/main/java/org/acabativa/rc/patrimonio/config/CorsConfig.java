package org.acabativa.rc.patrimonio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Usar allowedOriginPatterns para permitir todas as origens com wildcards
        config.setAllowedOriginPatterns(List.of("*"));

        // Permitir todos os metodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));

        // Permitir todos os headers
        config.setAllowedHeaders(List.of("*"));

        // Expor headers para o frontend
        config.setExposedHeaders(Arrays.asList("Content-Type", "Content-Length", "Authorization"));

        // Permitir credenciais (cookies, auth headers)
        config.setAllowCredentials(true);

        // Tempo de cache do preflight (1 hora)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplicar CORS para todos os endpoints
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
