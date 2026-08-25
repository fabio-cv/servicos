package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.dto.GeoLocationResponseDTO;
import com.labanta.servidorlocal.exception.GeoLocationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class GeoService {
    private final RestTemplate restTemplate;

    public GeoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeoLocationResponseDTO localizarIp(String ip){

        if (ip == null || ip.isBlank()){
            throw new IllegalArgumentException("IP não informado");
        }

        String url = "https://ipapi.co/" + ip + "/json/";

        GeoLocationResponseDTO resposta = restTemplate.getForObject(url, GeoLocationResponseDTO.class);

        if (resposta != null && Objects.equals(resposta.getIp(), ip)){
            return resposta;
        }

        throw new GeoLocationException("Ip inválido ou API indisponível!");
    }

}
