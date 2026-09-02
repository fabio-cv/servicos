package com.labanta.servidorlocal.dto;

public class ServicoRequestDTO {
    private String titulo;
    private String descricao;
    private double preco;


    public ServicoRequestDTO(String titulo, String descricao, double preco) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}