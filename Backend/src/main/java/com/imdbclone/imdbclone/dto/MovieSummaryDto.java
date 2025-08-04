package com.imdbclone.imdbclone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MovieSummaryDto {
    private UUID id;
    private String name;
    private String plot;
    private String poster;
    private Double ratings;
    private Integer yearOfRelease;
    private  String producerName;
    private List<String> actorNames;
}
