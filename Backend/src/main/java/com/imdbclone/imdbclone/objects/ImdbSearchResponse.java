package com.imdbclone.imdbclone.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImdbSearchResponse {
    private String imdbId;
    private String name;
    private String yearOfRelease;
    private String poster;
    private List<ImdbPerson> actors;
    private ImdbPerson producer;
    @JsonProperty("isImportAllowed")
    private boolean isImportAllowed;
}
