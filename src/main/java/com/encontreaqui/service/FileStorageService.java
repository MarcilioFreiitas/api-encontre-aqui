package com.encontreaqui.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    // Define a pasta onde os arquivos serão salvos
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    public FileStorageService() {
        // Cria a pasta "uploads" se não existir
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // Método para salvar o arquivo e retornar o nome único
    public String storeFile(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            Files.copy(file.getInputStream(),
                    Paths.get(uploadDir).resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo: " + e.getMessage());
        }
        return fileName;
    }
}
