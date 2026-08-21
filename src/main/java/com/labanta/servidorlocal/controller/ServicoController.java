package com.labanta.servidorlocal.controller;

import com.labanta.servidorlocal.dto.ServicoResponseDTO;
import com.labanta.servidorlocal.model.Servico;
import com.labanta.servidorlocal.service.ServicoService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ServicoController {
    private ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping("/servicos")
    public List<Servico> listarServicos(){
        return  servicoService.servicoFindAll();
    }

    @PostMapping("/servicos")
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

}
