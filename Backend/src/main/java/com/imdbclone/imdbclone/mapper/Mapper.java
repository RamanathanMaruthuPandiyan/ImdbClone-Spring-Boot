package com.imdbclone.imdbclone.mapper;

import com.imdbclone.imdbclone.dto.*;
import com.imdbclone.imdbclone.entity.Job;
import com.imdbclone.imdbclone.entity.Movie;
import com.imdbclone.imdbclone.entity.Person;
import com.imdbclone.imdbclone.enums.Gender;
import com.imdbclone.imdbclone.objects.DropDown;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class Mapper {

    /*-------------------------------------------------------------------------------------*/
    //Mapping to Pagination
    public static <T> PaginationDto<T> toPaginationDto(Page<T> page){
        return PaginationDto.<T>builder()
                .data(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .number(page.getNumber())
                .size(page.getSize())
                .numberOfElements(page.getNumberOfElements())
                .build();
    }


    /*-------------------------------------------------------------------------------------*/
    //Mapping to Entity's

    public static Person fromPersonDto(PersonDto personDto){
        return  Person.builder()
                .id(personDto.getId())
                .name(personDto.getName())
                .dob(personDto.getDob())
                .gender(Gender.fromCode(personDto.getGender()))
                .bio(personDto.getBio())
                .build();
    }

    public static Movie fromMovieDto(MovieDto movieDto){
        return Movie.builder()
                .name(movieDto.getName())
                .plot(movieDto.getPlot())
                .poster(movieDto.getPoster())
                .ratings(movieDto.getRatings())
                .yearOfRelease(movieDto.getYearOfRelease())
                .build();
    }

    public static Movie fromImdbImportDto(ImdbImportDto imdbImportDto){
        return Movie.builder()
                .imdbMovieId(imdbImportDto.getImdbId())
                .poster(imdbImportDto.getPoster())
                .yearOfRelease(imdbImportDto.getYearOfRelease())
                .name(imdbImportDto.getName())
                .build();
    }

    /*----------------------------------------------------------------------------------------*/
    // Mapping to Dto's

    public static PersonDto toPersonDto(Person person){
        return PersonDto.builder()
                .id(person.getId())
                .name(person.getName())
                .dob(person.getDob())
                .gender(person.getGender().getCode())
                .bio(person.getBio())
                .build();
    }

    public static MovieDto toMovieDto(Movie movie){
        return MovieDto.builder()
                .id(movie.getId())
                .name(movie.getName())
                .plot(movie.getPlot())
                .poster(movie.getPoster())
                .ratings(movie.getRatings())
                .yearOfRelease(movie.getYearOfRelease())
                .producerName(movie.getProducer() != null
                        ? new DropDown(
                        movie.getProducer().getId().toString(),
                        movie.getProducer().getName() + " - " + movie.getProducer().getDob())
                        : null)
                .actorNames(movie.getActors().stream().map(p->new DropDown(p.getId().toString(),
                        p.getName()+" - "+p.getDob())).toList())
                .build();
    }


    public  static PersonSummaryDto toPersonSummaryDto(Person person){
        return PersonSummaryDto.builder()
                .id(person.getId())
                .name(person.getName())
                .dob(person.getDob())
                .gender(person.getGender().getCode())
                .bio(person.getBio())
                .producedMovie(person.getProducedMovies().stream().map(p->p.getName()+" - "+p.getYearOfRelease()).collect(Collectors.toList()))
                .actedMovies(person.getActedMovies().stream().map(a->a.getName()+" - "+a.getYearOfRelease()).collect(Collectors.toList()))
                .build();
    }


    public static MovieSummaryDto toMovieSummaryDto(Movie movie) {
        return MovieSummaryDto.builder()
                .id(movie.getId())
                .name(movie.getName())
                .plot(movie.getPlot())
                .poster(movie.getPoster())
                .ratings(movie.getRatings())
                .yearOfRelease(movie.getYearOfRelease())
                .producerName(movie.getProducer()!=null?movie.getProducer().getName():null)
                .actorNames(movie.getActors().stream().map(Person::getName).collect(Collectors.toList()))
                .build();
    }

    public static  JobSummaryDto toJobSummaryDto(Job job){
        return JobSummaryDto.builder()
                .name(job.getName().getCode())
                .created(job.getCreated())
                .started(job.getStarted())
                .ended(job.getEnded())
                .status(job.getStatus().getCode())
                .message(job.getMessage())
                .reason(job.getReason())
                .completionPercentage(job.getCompletionPercentage())
                .build();
    }
}
