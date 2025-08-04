package com.imdbclone.imdbclone.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Builder
@ToString
public class PaginationDto<T> {
    private List<T> data;
    private Integer totalPages;
    private Long totalElements;
    private Integer size;
    private Integer number;
    private Integer numberOfElements;
}
