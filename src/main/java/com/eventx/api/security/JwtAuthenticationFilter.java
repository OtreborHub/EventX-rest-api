package com.eventx.api.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            String userId = jwtService.extractUserId(token);
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserPrincipal userDetails = (UserPrincipal) userDetailsService.loadUserByUsername(userId);
                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    logger.info("✅ Auth set: { "+ SecurityContextHolder.getContext().getAuthentication() + " }");
                }
            }
        } catch (JwtException | IllegalArgumentException ex) {
            // Token invalido: pulisco il contesto e lascio proseguire la chain.
            // Sarà Spring Security a rispondere 401 se l'endpoint è protetto.
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();
        String method = request.getMethod();

        boolean isPublic = path.equals("/api/v1/utenti")
                || path.equals("/api/v1/utenti/login")
                || path.equals("/api/v1/utenti/login/username")
                || (path.startsWith("/api/v1/eventi") && method.equals("GET"))
                || (path.startsWith("/api/v1/artisti") && method.equals("GET"))
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");

        return isPublic;
    }
}
