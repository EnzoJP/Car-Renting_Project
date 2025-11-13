package com.servidor.servidor.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final String uploadDir;

    public FileStorageService(@Value("${sprint.files.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio para almacenar los archivos.", ex);
        }
    }

    public String storeFile(MultipartFile file, String baseName) {
        if (file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        try {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
            extension = ".tmp";
        }

        String fileName = baseName.replaceAll("[^a-zA-Z0-9.-]", "_")
                + "_" + UUID.randomUUID().toString() + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Devolvemos SOLO el nombre del archivo
            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Error al almacenar el archivo " + fileName, ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Archivo no encontrado: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Archivo no encontrado: " + fileName, ex);
        }
    }

    public void deleteFile(String fileName) {
        try {
            if (fileName != null && !fileName.isEmpty()) {
                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
                Files.deleteIfExists(filePath);
            }
        } catch (IOException ex) {
            // No lanzar excepción si falla, solo loggear
            System.err.println("Error al eliminar el archivo: " + fileName);
        }
    }

    // Devuelve la ruta de la carpeta (útil si 'pathArchivo' necesita guardarse)
    public String getUploadDir() {
        return this.fileStorageLocation.toString();
    }
}
