package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.exception.CredenciasInvalidasException;
import com.labanta.servidorlocal.exception.UtilizadorExistenteException;
import com.labanta.servidorlocal.exception.UtilizadorNaoEncontradoException;
import com.labanta.servidorlocal.model.Utilizador;
import com.labanta.servidorlocal.repository.UtilizadorRepository;
import com.labanta.servidorlocal.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilizadorRepository repository;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(UtilizadorRepository repository, JwtService jwtService, EmailService emailService) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }


    public void saveUtilizador(Utilizador utilizador){
        repository.save(utilizador);
    }

    public String login(String username, String password){
        Utilizador utilizador = repository.findByUsername(username).orElseThrow(
                () ->  new UtilizadorNaoEncontradoException("O Utilizador com o username " + username + " não encontrado")
        );

        if (password.equals(utilizador.getPassword())){
            return this.jwtService.gerarTokens(utilizador.getUsername());
        }
        throw  new CredenciasInvalidasException("Username ou password inválidos");
    }


    public Utilizador registarUtilizador(String username, String email, String password){
        Utilizador utilizador = new Utilizador(username, email, password);
        boolean encontrado = repository.findByUsername(utilizador.getUsername()).isPresent();
        if (encontrado){
            throw new UtilizadorExistenteException("Utilizador com username " + username + " já existe! Não pode ter utilizadores com mesmo username!");
        }
        emailService.enviarEmailBoasVindas(utilizador.getEmail(), utilizador.getUsername());
        saveUtilizador(utilizador);
        return utilizador;
    }
}
