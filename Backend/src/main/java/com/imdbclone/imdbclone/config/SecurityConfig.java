package com.imdbclone.imdbclone.config;

import com.imdbclone.imdbclone.constants.RoleMaster;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/movies/*").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/movies/**").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/movies/**").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/movies/**").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/persons/*").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/persons/**").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/persons/**").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/persons/**").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/enums/*").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/s3/upload").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/s3/delete").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/imdb/search").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.POST,"/imdb/import").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/jobs/pagination").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/movies/pagination").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/movies/action/items/*").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/movies/filter/options").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/movies/dropdown/persons").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/persons/pagination").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/persons/action/items/*").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.GET,"/persons/filter/options").hasAuthority(RoleMaster.ADMIN)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/config").permitAll()
                        .anyRequest().denyAll()
                ) .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigins)); // 👈 frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
