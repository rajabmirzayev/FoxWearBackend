package com.foxwear.dynamicdataservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

/**
 * Service responsible for handling file operations with AWS S3.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    /**
     * Uploads a file to the configured S3 bucket.
     *
     * @param file The multipart file to upload.
     * @return The public URL of the uploaded file.
     */
    public String uploadFile(MultipartFile file) {

        // Generate a unique file name to prevent collisions
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        log.info("Starting file upload to S3: {}", fileName);

        try {
            // Prepare the S3 put request with public read access
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            log.info("Successfully uploaded file {} to bucket {}", fileName, bucketName);

            // Construct and return the public URL
            return String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName, "eu-north-1", fileName);
        } catch (IOException e) {
            log.error("Failed to upload file {} to S3: {}", fileName, e.getMessage());
            throw new RuntimeException("Could not upload file to S3", e);
        }
    }
}
