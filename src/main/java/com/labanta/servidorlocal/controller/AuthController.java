package com.labanta.servidorlocal.controller;

import com.labanta.servidorlocal.dto.GeoLocationResponseDTO;
import com.labanta.servidorlocal.dto.LoginRequestDTO;
import com.labanta.servidorlocal.dto.RegistoRequestDTO;
import com.labanta.servidorlocal.exception.CredenciasInvalidasException;
import com.labanta.servidorlocal.model.Utilizador;
import com.labanta.servidorlocal.repository.UtilizadorRepository;
import com.labanta.servidorlocal.security.JwtService;
import com.labanta.servidorlocal.service.AuthService;
import com.labanta.servidorlocal.service.EmailService;
import com.labanta.servidorlocal.service.GeoService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final GeoService geoService;
    private EmailService emailService;

    public AuthController(AuthService authService, GeoService geoService, EmailService emailService) {
        this.authService = authService;
        this.geoService = geoService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO login){
        return authService.login(login.getUsername(), login.getPassword());
    }

    @PostMapping("/registar")
    public Utilizador registar(@RequestBody RegistoRequestDTO registo){
        return authService.registarUtilizador(registo.getUsername(), registo.getEmail(), registo.getPassword());
    }

    @PostMapping("/alerta-login")
    public String alerta(@RequestParam String email, @RequestParam String ip){
        GeoLocationResponseDTO res = geoService.localizarIp(ip);
        emailService.enviarAlertaSeguranca(email, res.getCity(), res.getCountry_name());
        return "Alerta de segurança processado!";
    }

}
