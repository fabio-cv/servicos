package com.labanta.servidorlocal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final String dirUploads = "uploads/imagens";


    public FileStorageService() {
        try {
            // cria a pasta se não existir
            Files.createDirectory(Path.of(dirUploads));
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao criar pasta de uploads: " + e.getMessage());
        }
    }

    public String storeImage(MultipartFile file){

        try {
            // Gerar um nome único para o ficheiro
            String unicName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // definir caminho do ficheiro
            String outputPath = Paths.get(dirUploads).resolve(unicName).toString();

            Files.copy(file.getInputStream(), Paths.get(outputPath));

            return  unicName;


        } catch (Exception e){
            throw new RuntimeException("Erro ao carregar ficheiro.");
        }



    }



}
