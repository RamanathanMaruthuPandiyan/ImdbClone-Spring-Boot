package com.imdbclone.imdbclone.controller;

import com.imdbclone.imdbclone.dto.*;
import com.imdbclone.imdbclone.objects.DropDown;
import com.imdbclone.imdbclone.objects.FilterOption;
import com.imdbclone.imdbclone.objects.MovieFilter;
import com.imdbclone.imdbclone.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/action/items/{id}")
    public ResponseEntity<List<String>> getActionItems(@PathVariable("id") UUID id){
        List<String> actionItems = movieService.getActionItems(id);
        return ResponseEntity.ok(actionItems);
    }

    @GetMapping("/filter/options")
    public ResponseEntity<Map<String, FilterOption<?>>> getFilterOptions(){
        return ResponseEntity.ok(movieService.getFilterOptions());
    }

    @GetMapping("/dropdown/persons")
    public ResponseEntity<List<DropDown>> getPersonDropdown(){
        return ResponseEntity.ok(movieService.getPersonDropdown());
    }

    @GetMapping("/pagination")
    public PaginationDto<MovieSummaryDto> pagination(@RequestParam(required = false) String search,
                                                     @ParameterObject MovieFilter filter,
            @ParameterObject @PageableDefault(size = 15, sort = "name") Pageable pageable){
        return movieService.pagination(search,filter,pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable("id") UUID id){
        MovieDto result = movieService.getMovie(id);
        return  ResponseEntity.ok(result);
    }

    @PostMapping("/")
    public ResponseEntity<String> addMovie(@Valid @RequestBody MovieDto movieDto){
        String result = movieService.addMovie(movieDto);
        return  ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovie(@PathVariable("id") UUID id,@Valid @RequestBody MovieDto movieDto){
        String result = movieService.updateMovie(id,movieDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable("id") UUID id){
        String result  = movieService.deleteMovie(id);
        return ResponseEntity.ok(result);
    }
}
