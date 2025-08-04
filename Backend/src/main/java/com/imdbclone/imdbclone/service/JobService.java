package com.imdbclone.imdbclone.service;

import com.imdbclone.imdbclone.dto.JobSummaryDto;
import com.imdbclone.imdbclone.dto.PaginationDto;
import com.imdbclone.imdbclone.entity.Job;
import com.imdbclone.imdbclone.mapper.Mapper;
import com.imdbclone.imdbclone.repository.JobRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public PaginationDto<JobSummaryDto> pagination(String search, Pageable pageable) {
        Specification<Job> spec = (root, q, cb) -> {
            if (search != null && !search.isBlank()) {
                String likeSearch = "%" + search.toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("name").as(String.class)), likeSearch);
                Predicate reasonPredicate = cb.like(cb.lower(root.get("reason")), likeSearch);
                Predicate messagePredicate = cb.like(cb.lower(root.get("message")), likeSearch);

                return cb.or(namePredicate, reasonPredicate, messagePredicate);
            }
            return cb.conjunction();
        };
        Page<Job> page = jobRepository.findAll(spec,pageable);
        return Mapper.toPaginationDto(page.map(Mapper::toJobSummaryDto));
    }
}
