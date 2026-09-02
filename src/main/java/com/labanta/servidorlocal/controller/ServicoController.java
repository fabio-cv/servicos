package com.labanta.servidorlocal.controller;


import com.labanta.servidorlocal.dto.ServicoRequestDTO;
import com.labanta.servidorlocal.dto.ServicoResponseDTO;
import com.labanta.servidorlocal.model.Servico;
import com.labanta.servidorlocal.service.EmailService;
import com.labanta.servidorlocal.service.ExchangeService;
import com.labanta.servidorlocal.service.FileStorageService;
import com.labanta.servidorlocal.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/servicos")
public class ServicoController {
    private final ServicoService servicoService;
    private final ExchangeService exchangeService;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;


    public ServicoController(ServicoService servicoService, ExchangeService exchangeService, EmailService emailService, FileStorageService fileStorageService) {
        this.servicoService = servicoService;
        this.exchangeService = exchangeService;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
    }

    @Operation(
            summary = "Listar todos os serviços",
            description = "Rota para listar todos os serviços existentes na plataforma"
    )
    @GetMapping
    public ResponseEntity<Page<Servico>> listarServicos(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "id",
                    direction = Sort.Direction.DESC) Pageable pageable){
        Page<Servico> pagina = servicoService.getRepository().findAll(pageable);
        return ResponseEntity.ok(pagina);
    }




    @Operation(
            summary = "Criar um novo serviço",
            description = "Rota para criar um novo serviço"
    )
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping
    public Servico criarServico(@RequestBody ServicoRequestDTO servico){
        Servico novoServico = new Servico(servico.getTitulo(), servico.getDescricao(), servico.getPreco());
        return servicoService.saveServico(novoServico);
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


    @Operation(
            summary = "Upload de imagem de capa do serviço",
            description = "Carrega uma imagem multipart/form-data e associa à capa do serviço"
    )
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping(value = "/{id}/upload-capa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long id
            ){
        Servico servico =  servicoService.buscarServicoPorID(id);
        String fileuploaded = fileStorageService.storeImage(file);

        servico.setImagemCapa(fileuploaded);
        servicoService.saveServico(servico);

        return ResponseEntity.ok("Imagem carregada com sucesso: " + fileuploaded);
    }



}
