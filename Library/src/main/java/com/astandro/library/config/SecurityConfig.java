package com.astandro.library.config;

import com.astandro.library.filter.JwtAuthenticationFilter;
import com.astandro.library.service.UserService;
import com.astandro.library.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserServiceImpl userService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
    }

    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf().disable()
//                .authorizeHttpRequests() // For Spring Security 5.5+
//                .requestMatchers(
//                        "/api/auth/**",         // Allow login, registration, etc.
//                        "/swagger-ui/**",       // Allow Swagger UI
//                        "/v3/api-docs/**",      // Allow API docs
//                        "/swagger-ui.html",     // Allow Swagger HTML
//                        "/configuration/ui",
//                        "/swagger-resources/**",
//                        "/configuration/security",
//                        "/webjars/**",
//                        "/v3/**"
//                ).permitAll() // No authentication needed for these
//                .requestMatchers(HttpMethod.GET, "/api/articles/**")
//                .hasAnyRole("SUPER_ADMIN", "EDITOR", "CONTRIBUTOR", "VIEWER")
//                .requestMatchers(HttpMethod.POST, "/api/articles/**")
//                .hasAnyRole("SUPER_ADMIN", "EDITOR", "CONTRIBUTOR")
//                .anyRequest().authenticated() // Protect other endpoints
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
//                .and()
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers("/**").permitAll() // Allow Swagger UI and OpenAPI
                .anyRequest().authenticated() // Protect other endpoints
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class MethodSecurityConfig {
        // Enables @PreAuthorize annotations for RBAC
    }
}
