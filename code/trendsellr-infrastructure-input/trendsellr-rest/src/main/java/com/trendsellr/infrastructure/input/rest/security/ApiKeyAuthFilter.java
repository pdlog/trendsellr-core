package com.trendsellr.infrastructure.input.rest.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String apiKey;

    private final List<String> publicPaths;

    private final AppApiAuthenticationEntryPoint authenticationEntryPoint;

    public ApiKeyAuthFilter(@Value("${api.security.api-key}") final String apiKey,
                            @Value("${api.security.public-paths}") final List<String> publicPaths,
                            final AppApiAuthenticationEntryPoint authenticationEntryPoint) {
        this.apiKey = apiKey;
        this.publicPaths = publicPaths;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain)
            throws ServletException, IOException {

        if (this.isPublicPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String requestApiKey = request.getHeader("X-API-KEY");

        if (!this.apiKey.equals(requestApiKey)) {
            SecurityContextHolder.clearContext();
            this.authenticationEntryPoint.commence(request, response, new BadCredentialsException("Invalid or missing API Key"));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(final String path) {
        final AntPathMatcher pathMatcher = new AntPathMatcher();
        return this.publicPaths.stream().anyMatch(publicPath -> pathMatcher.match(publicPath, path));
    }
}