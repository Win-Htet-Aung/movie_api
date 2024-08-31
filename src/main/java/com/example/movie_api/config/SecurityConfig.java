package com.example.movie_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer -> customizer.disable());
        http.authorizeHttpRequests((requests) -> requests
        .requestMatchers(HttpMethod.GET,
        "/search", "/movies/**", "/series/**"
        ).permitAll()
        .requestMatchers(HttpMethod.POST,
        "/movies", "/series", "/seasons", "/episodes",
        "/genres", "/casts", "/productions", "/roles"
        ).hasAuthority("admin")
        .requestMatchers(HttpMethod.PUT,
        "/movies/*", "/series/*", "/seasons/*", "/episodes/*",
        "/genres/*", "/casts/*", "/productions/*", "/roles/*"
        ).hasAuthority("admin")
        .requestMatchers(HttpMethod.DELETE,
        "/movies/*", "/series/*", "/seasons/*", "/episodes/*",
        "/genres/*", "/casts/*", "/productions/*", "/roles/*"
        ).hasAuthority("admin")
        .anyRequest().authenticated()
        );
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }
}
