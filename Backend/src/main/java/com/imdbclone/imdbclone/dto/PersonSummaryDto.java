package com.imdbclone.imdbclone.dto;

import com.imdbclone.imdbclone.entity.Movie;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Builder
@ToString
public class PersonSummaryDto {
    private UUID id;
    private String name;
    private LocalDate dob;
    private String gender;
    private String bio;
    private List<String> producedMovie;
    private List<String> actedMovies;
}
