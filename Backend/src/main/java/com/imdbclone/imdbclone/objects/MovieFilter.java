package com.imdbclone.imdbclone.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieFilter {
    private List<Integer> yearOfRelease;
    private List<Double> ratings;
}
