package com.labanta.servidorlocal.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

   @ExceptionHandler(ServicoNaoEncontradoException.class)
   public ResponseEntity<Map<String, String>> handleServicoNaoEncontradoException(ServicoNaoEncontradoException ex){
       // Enviar um aviso ao administrador da plataforma
       log.warn("Tentativa de acesso a um recurso inexistente: {}", ex.getMessage());

       // JSON hashmap
       Map<String, String> resposta = new HashMap<>();
       resposta.put("erro", "Recurso nao encontrado");
       resposta.put("detalhes", ex.getMessage());

       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);

   }

   @ExceptionHandler(PercentagemDescontoException.class)
   public ResponseEntity<Map<String, String>> handlePercentagemDescontoException(PercentagemDescontoException ex) {
       log.warn("Argumento inválido: {}", ex.getMessage());

       Map<String, String> res = new HashMap<>();
       res.put("erro", "Desconto não pode ser menor/igual a 0 e nem maior que 100");
       res.put("mensagem", ex.getMessage());

       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
   }

   @ExceptionHandler(CredenciasInvalidasException.class)
    public ResponseEntity<Map<String, String>> handleCredenciasInvalidasException(CredenciasInvalidasException ex){
       log.warn("Algo deu errado: {}", ex.getMessage());
       Map<String, String> res = new HashMap<>();
       res.put("erro", "Credenciais inválidas!");
       res.put("mensagem", ex.getMessage());

       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
   }

   @ExceptionHandler(UtilizadorExistenteException.class)
    public ResponseEntity<Map<String, String>> handleUtilizadorExistenteException(UtilizadorExistenteException ex){
       log.warn("Erro: {}", ex.getMessage());

       Map<String, String> res = new HashMap<>();
       res.put("erro", "Utilizador com este username já existe!");
       res.put("mensagem", ex.getMessage());

       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
   }

    @ExceptionHandler(UtilizadorNaoEncontrado.class)
    public ResponseEntity<Map<String, String>> handleUtilizadorNaoEncontrado(UtilizadorNaoEncontrado ex){
        log.warn("Erro: {}", ex.getMessage());

        Map<String, String> res = new HashMap<>();
        res.put("erro", "Utilizador não encontrado!");
        res.put("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }
}
