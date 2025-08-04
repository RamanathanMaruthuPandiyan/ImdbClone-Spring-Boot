package com.imdbclone.imdbclone.controller;

import com.imdbclone.imdbclone.dto.ImdbImportDto;
import com.imdbclone.imdbclone.objects.ImdbSearchRequest;
import com.imdbclone.imdbclone.objects.ImdbSearchResponse;
import com.imdbclone.imdbclone.service.ImdbService;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@AllArgsConstructor
@RequestMapping("/imdb")
public class ImdbController {
    private final ImdbService imdbService;

    @GetMapping("/search")
    public ResponseEntity<Flux<ImdbSearchResponse>> getSearch(@ParameterObject ImdbSearchRequest imdbSearchRequest){
        Flux<ImdbSearchResponse> result = imdbService.getSearch(imdbSearchRequest);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/import")
    public ResponseEntity<String>  importFromImdb(@RequestBody ImdbImportDto imdbImportDto){
        imdbService.importFromImdb(imdbImportDto);
        return ResponseEntity.ok("Process initiated to import the movie from IMDB. Please check the job status later.");
    }
}
