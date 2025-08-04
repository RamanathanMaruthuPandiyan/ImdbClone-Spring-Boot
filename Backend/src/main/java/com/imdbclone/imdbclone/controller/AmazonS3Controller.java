package com.imdbclone.imdbclone.controller;

import com.imdbclone.imdbclone.dto.S3UploadResponse;
import com.imdbclone.imdbclone.service.AmazonS3Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/s3")
public class AmazonS3Controller {
    private final AmazonS3Service amazonS3Service;

    public AmazonS3Controller(AmazonS3Service amazonS3Service) {
        this.amazonS3Service = amazonS3Service;
    }

    @PostMapping("/upload")
    public S3UploadResponse uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonS3Service.uploadFile(file);
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonS3Service.deleteFileFromS3Bucket(fileUrl);
    }

}
