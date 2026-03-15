package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.service.FileService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<@NonNull ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String imageUrl = fileService.uploadFile(file);
        return ResponseEntity.ok(ApiResponse.success(imageUrl));
    }
}
