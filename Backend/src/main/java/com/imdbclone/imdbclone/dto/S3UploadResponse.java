package com.imdbclone.imdbclone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class S3UploadResponse {
    private String fileUrl;
    private String message;
}
