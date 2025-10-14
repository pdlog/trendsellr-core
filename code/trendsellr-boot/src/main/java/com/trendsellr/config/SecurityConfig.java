package com.trendsellr.config;

import com.trendsellr.infrastructure.input.rest.security.ApiKeyAuthFilter;
import com.trendsellr.infrastructure.input.rest.security.AppUserAuthenticationEntryPoint;
import com.trendsellr.infrastructure.input.rest.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApiKeyAuthFilter apiKeyAuthFilter;

    private final JwtAuthFilter jwtAuthFilter;

    private final AppUserAuthenticationEntryPoint appAuthenticationEntryPoint;

    @Value("${api.security.public-paths}")
    private final List<String> publicPaths;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(this.appAuthenticationEntryPoint)
                )
                .anonymous(anonymous -> anonymous.authorities("ROLE_ANONYMOUS"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(this.publicPaths.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(this.apiKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(this.jwtAuthFilter, ApiKeyAuthFilter.class)
                .build();
    }
}