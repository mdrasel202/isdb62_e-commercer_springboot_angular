package com.rasel.security_demo.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService  {
    private final Path rootLocation;

    public FileStorageService(@Value("${app.profile-images.dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
    }

    public void init(){
        try{
            Files.createDirectories(rootLocation);
        }catch (IOException e){
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String store(MultipartFile file, Long userId){
         try{
             if(file.isEmpty()){
                 throw new RuntimeException("Failed not store empty file");
             }
             //validate file type
             String contentType = file.getContentType();
             if(contentType == null || !contentType.startsWith("image/")){
                 throw new RuntimeException("Only image files are allowed");
             }
             //validate file size (max 5mp)
             if(file.getSize() > 5 * 1024 * 1024){
                 throw new RuntimeException("File size exceeds 5mb limit");
             }

             String originalFilename = file.getOriginalFilename();
             String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
             String newFilename = "profile" + fileExtension;

             Path userDir = rootLocation.resolve(userId.toString());
             Files.createDirectories(userDir);

             Path destinationFile = userDir.resolve(newFilename)
                     .normalize()
                     .toAbsolutePath();
             try(InputStream inputStream = file.getInputStream()){
                 Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
             }

             return newFilename;
         }catch (IOException e){
             throw new RuntimeException("Failed to store file", e);
         }
    }
}
