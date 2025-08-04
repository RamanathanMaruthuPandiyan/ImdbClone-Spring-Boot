package com.imdbclone.imdbclone.controller;

import com.imdbclone.imdbclone.dto.JobSummaryDto;
import com.imdbclone.imdbclone.dto.PaginationDto;
import com.imdbclone.imdbclone.service.JobService;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;

    @GetMapping("/pagination")
    public PaginationDto<JobSummaryDto> pagination(@RequestParam(required = false) String search,
                                                   @ParameterObject Pageable pageable){
        return jobService.pagination(search,pageable);
    }
}
