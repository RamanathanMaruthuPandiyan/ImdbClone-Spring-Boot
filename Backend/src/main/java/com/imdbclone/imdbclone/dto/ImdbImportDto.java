package com.imdbclone.imdbclone.dto;

import com.imdbclone.imdbclone.objects.ImdbPerson;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ImdbImportDto {
    private String imdbId;
    private String name;
    private Integer yearOfRelease;
    private String poster;
    private List<ImdbPerson> actors;
}
