package com.imdbclone.imdbclone.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImdbSearchRequest {
    private String search;
    private String type="MOVIE";
    private Integer limit=10;
    private String language="en-US";
}
