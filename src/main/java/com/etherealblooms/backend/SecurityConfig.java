package com.etherealblooms.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configure CORS to allow requests from both local and live frontend
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                // THIS IS THE GLOW-UP: Add your live Vercel URL to the guest list
                config.setAllowedOrigins(List.of(
                    "http://localhost:3000",
                    "https://ethereal-blooms-frontend.vercel.app" // Make sure this matches your Vercel URL
                ));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }))
            // Disable CSRF for stateless APIs
            .csrf(csrf -> csrf.disable())
            // Define authorization rules for your API endpoints
            .authorizeHttpRequests(auth -> auth
                // These endpoints are public and don't require a login
                .requestMatchers(
                    "/api/auth/**",      // For login and signup
                    "/api/send-email",   // For the public contact form
                    "/api/testimonials"  // So everyone can see reviews
                ).permitAll()
                // Any other request must be authenticated (e.g., leaving a review)
                .anyRequest().authenticated()
            )
            // Use stateless sessions because we are using JWTs
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

