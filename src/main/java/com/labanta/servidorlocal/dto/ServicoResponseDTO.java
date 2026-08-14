package com.labanta.servidorlocal.dto;

public class ServicoResponseDTO {
    private String titulo;
    private double precoFinal;

    public ServicoResponseDTO(String titulo, double precoFinal) {
        this.titulo = titulo;
        this.precoFinal = precoFinal;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPrecoFinal() {
        return precoFinal;
    }

    public void setPrecoFinal(double precoFinal) {
        this.precoFinal = precoFinal;
    }
}
