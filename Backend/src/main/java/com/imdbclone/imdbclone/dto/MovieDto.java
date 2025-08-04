package com.imdbclone.imdbclone.dto;

import com.imdbclone.imdbclone.objects.DropDown;
import jakarta.validation.constraints.*;
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
public class MovieDto {

    private UUID id;

    @NotBlank(message = "Name can't be a empty.")
    @NotNull(message = "Name can't be null.")
    private String name;

    @NotBlank(message = "Plot can't be a empty.")
    @NotNull(message = "Plot can't be null.")
    private String plot;

    @NotBlank(message = "Poster can't be a empty.")
    @NotNull(message = "Poster can't be null.")
    private String poster;

    @Min(value = 3,message = "Rating must be at least 3.")
    @Max(value = 10,message = "Rating must not exceed 10.")
    @NotNull(message = "Ratings can't be null.")
    private Double ratings;

    @Digits(integer = 4,fraction = 0, message = "Year of release must be a 4-digit number.")
    @NotNull(message = "Year of Release can't be null.")
    private Integer yearOfRelease;

    @NotNull(message = "Producer field can't be null.")
    private  UUID producerId;

    @NotNull(message = "Actor's field can't be null.")
    private Set<UUID> actorIds;

    private DropDown producerName;
    private List<DropDown> actorNames;
}
