package com.labanta.servidorlocal.dto;

import java.util.Map;

public class ExchangeRateResponseDTO {
    private String base;
    private Map<String, Double> rates;


    public String getBase() {
        return base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }
}
