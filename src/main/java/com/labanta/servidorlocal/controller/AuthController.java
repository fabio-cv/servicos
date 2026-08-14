package com.labanta.servidorlocal.controller;

import com.labanta.servidorlocal.dto.LoginRequestDTO;
import com.labanta.servidorlocal.exception.CredenciasInvalidasException;
import com.labanta.servidorlocal.security.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class AuthController {

    @PostMapping("/api/v1/login")
    public String login(@RequestBody LoginRequestDTO login){

        if (Objects.equals(login.getUsername(), "admin") && Objects.equals(login.getPassword(), "12345")){
            JwtService token = new JwtService();
            return token.gerarTokens(login.getUsername());
        }
        throw  new CredenciasInvalidasException("Username ou password inválidos");
    }
}
