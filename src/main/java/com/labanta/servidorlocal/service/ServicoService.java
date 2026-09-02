package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.exception.PercentagemDescontoException;
import com.labanta.servidorlocal.exception.ServicoNaoEncontradoException;
import com.labanta.servidorlocal.model.Servico;
import com.labanta.servidorlocal.repository.ServicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicoService {
    private final ServicoRepository repository;
    private static final Logger log = LoggerFactory.getLogger(ServicoService.class);


    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    public ServicoRepository getRepository() {
        return repository;
    }

    public Servico saveServico(String titulo, String descricao, double preco){
        Servico servico = new Servico(titulo, descricao, preco, true, null, null);
       return repository.save(servico);
    }

    public List<Servico> servicoFindAll(){
        return repository.findAll();
    }

    public Servico buscarServicoPorID(Long id){

        return repository.findById(id).orElseThrow(
                ()-> new ServicoNaoEncontradoException("O serviço com o ID " + id + " não existe no catálogo.")
        );

    }




    public List<Servico> aplicarDescontoEmAtivos(double percentagem){

        if (percentagem <= 0 || percentagem > 100){
            throw new PercentagemDescontoException("Desconto inválido");
        }

        final List<Servico> listaAtualizada = repository.findByEstadoTrue();
        log.info("A iniciar o cálculo de descontos...");
        for (Servico servico: listaAtualizada){
            if (servico.getPreco() >= 10000 && percentagem == 10){
                double novoPreco = servico.getPreco() - (servico.getPreco() * percentagem/100);
                servico.setPrecoComDesconto(novoPreco);
            }
        }
        return repository.saveAll(listaAtualizada);
    }

    public List<Servico> buscarServicoPeloTitulo(String titulo){
        return repository.findByTituloContainingIgnoreCase(titulo);
    }
}
