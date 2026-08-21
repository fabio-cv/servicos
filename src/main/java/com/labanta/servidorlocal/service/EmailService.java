package com.labanta.servidorlocal.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void enviarEmailBoasVindas(String emailDestino, String nomeUtilizador){
        // Criar um email simples(texto limpo)
        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom(emailDestino);
        mensagem.setTo(emailDestino);
        mensagem.setSubject("bem vindo ao Marketplace!");
        mensagem.setText("Olá " + nomeUtilizador + "!\n\n" +
                "A tua conta foi criada com sucesso. Já podes fazer login e explorar os nossos serviços. \n\n" +
                "Com os melhores cumprimentos. \nEquipa do MarketPlace");

        mailSender.send(mensagem);
    }
}
