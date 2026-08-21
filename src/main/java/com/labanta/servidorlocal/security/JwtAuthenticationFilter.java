package com.labanta.servidorlocal.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;

    public  JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        log.info("=== JWT Filter === Path: {} | Authorization header presente: {}", request.getRequestURI(), authHeader != null);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        //E
        // Extrair o token (ignorar os primeiros 7 caracteres: "Bearer ")
        String token = authHeader.substring(7);
        if (token.isEmpty() || token.equals("undefined")) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            //EXTRAIR O USERNAME DO TOKEN (ISTO TAMBEM VALIDA A ASSINATURA E A )
            String username = jwtService.extrairUsername(token);
            log.info("=== JWT Filter === Username extraído do token: {}", username);
            //SE O USERNAME E VALIDO E AINDA NAO HA AUTENTICACAO NO CONTEXTO
            if (username !=null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //DIZER AO SPRING QUE ESTE UTILIZADOR ESTA AUTENTICADO
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>()) ;
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("=== JWT Filter === Utilizador autenticado com sucesso: {}", username);
            }
        }catch (Exception e){
            //TOKEN INVALIDO AO EXPIRADO -NAO AUTENTICAR, O SPRING VAI DEVOLVER 401
            log.error("=== JWT Filter === Erro ao validar token: {}", e.getMessage(), e);
        }
        filterChain.doFilter(request, response);
    }
}