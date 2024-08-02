package com.knu_polije.project.domain.storage.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.knu_polije.project.domain.storage.exception.StorageErrorCode;
import com.knu_polije.project.global.exception.GlobalException;
import com.knu_polije.project.global.util.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StorageService {
    private static final Path storageLocation = Paths.get("/storage/images");

    public String storeImage(MultipartFile image) {
        try {
            if (image.isEmpty()) {
                throw new GlobalException(StorageErrorCode.NOT_FOUND_FILE);
            }
            if (!FileUtil.isValidImageFile(image.getInputStream())) {
                throw new GlobalException(StorageErrorCode.UNSUPPORTED_FILE_EXTENSION);
            }
            String randomizedFilename =
                    FileUtil.convertToRandomFilename(image.getOriginalFilename());
            Path destination = storageLocation.resolve(Paths.get(randomizedFilename)).normalize()
                    .toAbsolutePath();
            if (!destination.getParent().equals(storageLocation.toAbsolutePath())) {
                throw new GlobalException(StorageErrorCode.INVALID_FILE_PATH);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            return randomizedFilename;
        } catch (IOException exception) {
            throw new GlobalException(StorageErrorCode.FAILED_FILE_STORE);
        }
    }

    private Path load(String fileName) {
        return storageLocation.resolve(fileName);
    }

    public Resource loadAsResource(String fileName) {
        try {
            Path path = load(fileName);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new GlobalException(StorageErrorCode.NOT_FOUND_FILE);
            }
        } catch (MalformedURLException e) {
            throw new GlobalException(StorageErrorCode.INVALID_FILE_PATH);
        }
    }
}
