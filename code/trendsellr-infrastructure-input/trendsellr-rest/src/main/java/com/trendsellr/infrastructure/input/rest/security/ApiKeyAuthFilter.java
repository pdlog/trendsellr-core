package com.trendsellr.infrastructure.input.rest.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String apiKey;

    private final AppAuthenticationEntryPoint authenticationEntryPoint;

    public ApiKeyAuthFilter(@Value("${api.security.api-key}") final String apiKey,
                            final AppAuthenticationEntryPoint authenticationEntryPoint) {
        this.apiKey = apiKey;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final FilterChain filterChain)
            throws ServletException, IOException {

        final String requestApiKey = request.getHeader("X-API-KEY");

        if (!this.apiKey.equals(requestApiKey)) {
            SecurityContextHolder.clearContext();

            BadCredentialsException authException = new BadCredentialsException("Invalid or missing API Key");

            this.authenticationEntryPoint.commence(request, response, authException);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("api-client", null, null)
        );

        filterChain.doFilter(request, response);
    }
}