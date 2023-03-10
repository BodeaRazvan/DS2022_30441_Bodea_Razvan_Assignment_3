package com.energy.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private Object JwtAuthFilter;

    public JwtAuthenticationFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
    }

    @Override
    public Authentication attemptAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        //System.out.println(request.getRequestURI());
        if(request.getRequestURI().contains("login") || request.getRequestURI().contains("register") || request.getRequestURI().contains("ws")) {
            //System.out.println("Allowed");
            return null;
        }
        if(request.getRequestURI().contains("websocket") || request.getRequestURI().contains("favicon.ico")) {
            //System.out.println("Allowed");
            return null;
        }
        final String param = request.getHeader("Authorization");
        if(param == null || param.isEmpty()) {
            throw new RuntimeException("Authorization header is required");
        }
        String token = param.trim();
        final Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
        return getAuthenticationManager()
                .authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult
    ) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

}