package com.knu_polije.project.domain.storage.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.knu_polije.project.domain.storage.service.StorageService;
import com.knu_polije.project.global.util.ApiUtil;
import com.knu_polije.project.global.util.ApiUtil.ApiSuccessResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    /**
     * 이미지 업로드
     * 
     * @param image 이미지 파일
     * @return 이미지 업로드 성공 시 이미지 파일명
     */
    @PostMapping("/images")
    public ResponseEntity<ApiSuccessResult<String>> uploadImage(
            @RequestPart MultipartFile image
    ) {
        String imageName = storageService.storeImage(image);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiUtil.success(HttpStatus.OK, imageName));
    }

    /**
     * 이미지 조회
     * 
     * @param imageFileName 이미지 파일명
     * @return 이미지 파일
     */
    @GetMapping(
            value = "/images/{imageFileName}",
            produces = {
                    MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE
            }
        )
    public ResponseEntity<Resource> getImage(
            @PathVariable String imageFileName
    ) {
        Resource file = storageService.loadAsResource(imageFileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline").body(file);
    }
}
