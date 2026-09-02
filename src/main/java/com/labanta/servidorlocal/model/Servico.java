package com.labanta.servidorlocal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Servico {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private double preco;
    private boolean estado;
    private Double precoComDesconto;
    private String imagemCapa;


    public Servico() {
    }

    public Servico(String titulo, String descricao, double preco) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.estado = true;
    }

    public Servico(String titulo, String descricao, double preco, boolean estado, Double precoComDesconto, String imagemCapa) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.estado = estado;
        this.precoComDesconto = precoComDesconto;
        this.imagemCapa = imagemCapa;
    }

    public Long getId() {
        return id;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Double getPrecoComDesconto() {
        return precoComDesconto;
    }

    public void setPrecoComDesconto(Double precoComDesconto) {
        this.precoComDesconto = precoComDesconto;
    }

    public String getImagemCapa(){
        return imagemCapa;
    }

    public void setImagemCapa(String imagemCapa){
        this.imagemCapa = imagemCapa;
    }
}
