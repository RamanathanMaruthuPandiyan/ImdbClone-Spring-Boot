package com.imdbclone.imdbclone.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class JobSummaryDto {
    private String name;
    private LocalDateTime created;
    private LocalDateTime started;
    private LocalDateTime ended;
    private Integer completionPercentage;
    private String reason;
    private String message;
    private String status;
}
