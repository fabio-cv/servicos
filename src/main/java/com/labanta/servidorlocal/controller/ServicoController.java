package com.labanta.servidorlocal.controller;

import com.labanta.servidorlocal.dto.ExchangeRateResponseDTO;
import com.labanta.servidorlocal.dto.ServicoResponseDTO;
import com.labanta.servidorlocal.model.Servico;
import com.labanta.servidorlocal.service.EmailService;
import com.labanta.servidorlocal.service.ExchangeService;
import com.labanta.servidorlocal.service.ServicoService;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/servicos")
public class ServicoController {
    private final ServicoService servicoService;
    private final ExchangeService exchangeService;
    private final EmailService emailService;


    public ServicoController(ServicoService servicoService, ExchangeService exchangeService, EmailService emailService) {
        this.servicoService = servicoService;
        this.exchangeService = exchangeService;
        this.emailService = emailService;
    }

    @GetMapping
    public List<Servico> listarServicos(){
        return  servicoService.servicoFindAll();
    }

    @PostMapping
    public Servico criarServico(@RequestBody Servico servico){
        return servicoService.saveServico(servico);
    }

    @GetMapping("/{id}")
    public Servico obterServicoPorID(@PathVariable Long id){
        return servicoService.buscarServicoPorID(id);
    }

    @PostMapping("/aplicar-desconto")
    public List<ServicoResponseDTO> aplicarDesconto(@RequestBody double desconto){
        List<Servico> lista = servicoService.aplicarDescontoEmAtivos(desconto);
        List<ServicoResponseDTO> listaComPrecoFinal = new ArrayList<>();

        for (Servico s: lista){
            ServicoResponseDTO servicoResponseDTO = new ServicoResponseDTO(s.getTitulo(), s.getPrecoComDesconto());
            listaComPrecoFinal.add(servicoResponseDTO);
        }
        return listaComPrecoFinal;

    }

    @GetMapping("/pesquisa")
    public List<Servico> buscarServico(@RequestParam String termo){
        return servicoService.buscarServicoPeloTitulo(termo);
    }



    @PostMapping("/{id}/orcamento")
    public String pedirOrcamento(
            @PathVariable Long id,
            @RequestParam String emailDestino,
            @RequestParam(defaultValue = "CVE") String moeda
    ){
        // 1. Ir à Base de Dados buscar o Serviço
        Servico servico = servicoService.buscarServicoPorID(id);

        // 2. Ir à internet converter o preço
        Double precoConvertido = exchangeService.converterPreco(servico.getPreco(), moeda);

        // 3. Enviar o resultado para o Gmail do cliente
        emailService.enviarOrcamentoPorEmail(emailDestino, servico.getTitulo(), precoConvertido, moeda);

        return  "Orcamento calculado e enviado com sucesso para " + emailDestino + "!";
    }

}
