package com.labanta.servidorlocal.controller;

import com.labanta.servidorlocal.dto.ExchangeRateResponseDTO;
import com.labanta.servidorlocal.dto.ServicoResponseDTO;
import com.labanta.servidorlocal.model.Servico;
import com.labanta.servidorlocal.service.EmailService;
import com.labanta.servidorlocal.service.ExchangeService;
import com.labanta.servidorlocal.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            summary = "Listar todos os serviços",
            description = "Rota para listar todos os serviços existentes na plataforma"
    )
    @GetMapping
    public List<Servico> listarServicos(){
        return  servicoService.servicoFindAll();
    }




    @Operation(
            summary = "Criar um novo serviço",
            description = "Rota para criar um novo serviço"
    )
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping
    public Servico criarServico(@RequestBody Servico servico){
        return servicoService.saveServico(servico);
    }



    @Operation(
            summary = "Obter um serviço pelo ID",
            description = "Rota para buscar/obter um determinado serviço pelo ID"
    )
    @GetMapping("/{id}")
    public Servico obterServicoPorID(@PathVariable Long id){
        return servicoService.buscarServicoPorID(id);
    }

    @Operation(
            summary = "Aplicar desconto nos serviços",
            description = "Aplica uma percentagem de desconto a todos os serviços ativos"
    )
    @SecurityRequirement(name = "BearerAuth")
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

    @Operation(
            summary = "Pesquisar serviços",
            description = "Pesquisa serviços por termo/título"
    )
    @GetMapping("/pesquisa")
    public List<Servico> buscarServico(@RequestParam String termo){
        return servicoService.buscarServicoPeloTitulo(termo);
    }



    @Operation(
            summary = "Pedir orçamento de serviço",
            description = "Calcula a conversão de moeda do preço do serviço e envia o orçamento por email"
    )
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/{id}/orcamento")
    public String pedirOrcamento(
            @PathVariable Long id,
            @RequestParam String emailDestino,
            @RequestParam(defaultValue = "CVE") String moeda
    ){

        Servico servico = servicoService.buscarServicoPorID(id);

        Double precoConvertido = exchangeService.converterPreco(servico.getPreco(), moeda);
        emailService.enviarOrcamentoPorEmail(emailDestino, servico.getTitulo(), precoConvertido, moeda);

        return  "Orcamento calculado e enviado com sucesso para " + emailDestino + "!";
    }

}
