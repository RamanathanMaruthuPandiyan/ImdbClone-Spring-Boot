package com.imdbclone.imdbclone.controller;

import com.imdbclone.imdbclone.dto.*;
import com.imdbclone.imdbclone.objects.FilterOption;
import com.imdbclone.imdbclone.objects.PersonFilter;
import com.imdbclone.imdbclone.service.PersonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @GetMapping("/action/items/{id}")
    public ResponseEntity<List<String>> getActionItems(@PathVariable("id") UUID id){
        List<String> actionItems = personService.getActionItems(id);
        return ResponseEntity.ok(actionItems);
    }

    @GetMapping("/filter/options")
    public ResponseEntity<Map<String, FilterOption<?>>> getFilterOptions(){
        return ResponseEntity.ok(personService.getFilterOptions());
    }

    @GetMapping("/pagination")
    public PaginationDto<PersonSummaryDto> pagination(@RequestParam(required = false) String search,
                                                      @ParameterObject PersonFilter filter,
                                                      @ParameterObject @PageableDefault(size = 20,
            sort = "name") Pageable pageable){
        return personService.pagination(search,filter,pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPerson(@Valid @PathVariable("id") UUID id){
        PersonDto result = personService.getPerson(id);
        return  ResponseEntity.status(200).body(result);
    }


    @PostMapping("/")
    public ResponseEntity<String> addPerson(@Valid @RequestBody PersonDto personDto){
            String result = personService.addPerson(personDto);
            return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePerson(@PathVariable("id") UUID id, @Valid @RequestBody PersonDto personDto){
        String result = personService.updatePerson(id,personDto);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable("id") UUID id){
        String result  = personService.deletePerson(id);
        return ResponseEntity.ok(result);
    }
}
