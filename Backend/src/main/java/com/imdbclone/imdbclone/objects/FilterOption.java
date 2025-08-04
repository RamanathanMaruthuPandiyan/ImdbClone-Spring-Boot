package com.imdbclone.imdbclone.objects;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FilterOption<T> {
    private String displayName;
    private List<T> options;
}
