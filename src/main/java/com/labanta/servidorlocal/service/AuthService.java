package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.dto.LoginRequestDTO;
import com.labanta.servidorlocal.dto.RegistoRequestDTO;
import com.labanta.servidorlocal.exception.CredenciasInvalidasException;
import com.labanta.servidorlocal.exception.UtilizadorExistenteException;
import com.labanta.servidorlocal.exception.UtilizadorNaoEncontrado;
import com.labanta.servidorlocal.model.Utilizador;
import com.labanta.servidorlocal.repository.UtilizadorRepository;
import com.labanta.servidorlocal.security.JwtService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

    private final UtilizadorRepository repository;



    public AuthService(UtilizadorRepository repository) {
        this.repository = repository;
    }

    public void saveUtilizador(Utilizador utilizador){
        repository.save(utilizador);
    }

    public String login(String username, String password){
        Utilizador utilizador = repository.findByUsername(username).orElseThrow(
                () ->  new UtilizadorNaoEncontrado("O Utilizador com o username " + username + " não encontrado")
        );

        if (password.equals(utilizador.getPassword())){
            JwtService token = new JwtService();
            return token.gerarTokens(utilizador.getUsername());
        }
        throw  new CredenciasInvalidasException("Username ou password inválidos");
    }


    public Utilizador registarUtilizador(String username, String email, String password){
        Utilizador utilizador = new Utilizador(username, email, password);
        boolean encontrado = repository.findByUsername(utilizador.getUsername()).isPresent();
        if (encontrado){
            throw new UtilizadorExistenteException("Utilizador com username " + username + " já existe! Não pode ter utilizadores com mesmo username!");
        }
        saveUtilizador(utilizador);
        return utilizador;
    }
}
