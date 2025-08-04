package com.imdbclone.imdbclone.service;

import com.imdbclone.imdbclone.dto.*;
import com.imdbclone.imdbclone.entity.Lock;
import com.imdbclone.imdbclone.entity.Movie;
import com.imdbclone.imdbclone.entity.Person;
import com.imdbclone.imdbclone.enums.ActionItems;
import com.imdbclone.imdbclone.exceptions.BadRequestException;
import com.imdbclone.imdbclone.exceptions.ConflictException;
import com.imdbclone.imdbclone.exceptions.InternalServerErrorException;
import com.imdbclone.imdbclone.exceptions.ResourceNotFoundException;
import com.imdbclone.imdbclone.mapper.Mapper;
import com.imdbclone.imdbclone.objects.DropDown;
import com.imdbclone.imdbclone.objects.FilterOption;
import com.imdbclone.imdbclone.objects.MovieFilter;
import com.imdbclone.imdbclone.repository.LockRepository;
import com.imdbclone.imdbclone.repository.MovieRepository;
import com.imdbclone.imdbclone.repository.PersonRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    private final PersonRepository personRepository;

    private final LockRepository lockRepository;

    public PaginationDto<MovieSummaryDto> pagination(String search, MovieFilter filter, Pageable pageable) {
        Specification<Movie> spec = (root,q,cb)->{
          List<Predicate> predicates=new ArrayList<>();

          if(search!=null && !search.isBlank()){
              predicates.add( cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
          }

          if (filter.getYearOfRelease() != null && !filter.getYearOfRelease().isEmpty()) {
              predicates.add( root.get("yearOfRelease").in(filter.getYearOfRelease()));
          }

          if (filter.getRatings() != null && !filter.getRatings().isEmpty()) {
              predicates.add( root.get("ratings").in(filter.getRatings()));
          }

          return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Movie> page = movieRepository.findAll(spec,pageable);
        return Mapper.toPaginationDto(page.map(Mapper::toMovieSummaryDto));
    }

    public MovieDto getMovie(UUID id) {
        Optional<Movie> movie = movieRepository.findById(id);
        if(movie.isEmpty()){
            throw new ResourceNotFoundException("Received invalid id.");
        }
        return Mapper.toMovieDto(movie.get());
    }

    public List<String> getActionItems(UUID id) {
        boolean isMovieRecordExist = movieRepository.existsById(id);

        if(!isMovieRecordExist){
            throw new ResourceNotFoundException("Received invalid id.");
        }

        List<String> actionItems = new ArrayList<>(Arrays.asList(ActionItems.EDIT.getCode(),ActionItems.DELETE.getCode()));

        return  actionItems;
    }

    public List<DropDown> getPersonDropdown() {
        return personRepository.findAll().stream().map(p->new DropDown(p.getId().toString(),p.getName()+" - "+p.getDob())).toList();
    }

    public Map<String, FilterOption<?>> getFilterOptions() {
        List<Integer> yearOfRelease = movieRepository.findDistinctYearsOfRelease();
        List<Double> ratings = movieRepository.findDistinctRatings();

        Map<String, FilterOption<?>> filterOptions = Map.of(
                "yearOfRelease",new FilterOption<>("Year of Release",yearOfRelease),
                "ratings", new FilterOption<>("Ratings",ratings)
        );

        return filterOptions;
    }

    private Movie getMovieObject(MovieDto movieDto) {
        Person producer = personRepository.findById(movieDto.getProducerId())
                .orElseThrow(() -> new ResourceNotFoundException("Provided producer is invalid."));

        Set<Person> actors = new HashSet<>(personRepository.findAllById(movieDto.getActorIds()));

        if(movieDto.getActorIds().size() !=actors.size()){
            throw new ResourceNotFoundException("One or more provided actors are invalid.");
        }

        Movie movie = Mapper.fromMovieDto(movieDto);
        movie.setProducer(producer);
        movie.setActors(actors);

        return movie;
    }

    public String addMovie(MovieDto movieDto) {
        Movie movie = getMovieObject(movieDto);

        boolean isNameExist = movieRepository.existsByName(movie.getName());

        if (isNameExist) {
            throw new ConflictException("Movie with name '" + movie.getName() + "' already exists.");
        }

        Movie resultEntity = movieRepository.save(movie);

        if (resultEntity.getId() == null) {
            throw new InternalServerErrorException("Error while saving the movie '" + movie.getName() + "'.");
        }

        return "The Movie: '" + movie.getName() + "' was added successfully.";
    }

    public String updateMovie(UUID id, MovieDto movieDto) {
        Movie existingMovieRecord = movieRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Movie doesn't exists."));

        Movie movie = getMovieObject(movieDto);

        boolean isNameExist = movieRepository.existsByNameAndIdNot(movie.getName(),id);

        if (isNameExist) {
            throw new ConflictException("Movie with name '" + movie.getName() + "' already exists.");
        }

        movie.setId(id);

        boolean isUnChanged = existingMovieRecord.equals(movie);

        if (isUnChanged){
            throw new BadRequestException("No modifications found.");
        }

        Movie resultEntity = movieRepository.save(movie);

        if (resultEntity.getId() == null) {
            throw new InternalServerErrorException("Error while updating the movie '" + movie.getName() + "'.");
        }

        return "The Movie: '" + movie.getName() + "' was updated successfully.";
    }

    @Transactional
    public String deleteMovie(UUID id) {
        try{
            Movie existingMovieRecord = movieRepository.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("Movie doesn't exists."));
            if (existingMovieRecord.getImdbMovieId() != null) {
                Optional<Lock> lock = lockRepository.findByResourceId(existingMovieRecord.getImdbMovieId());
                lock.ifPresent(lockRepository::delete);
            }
            movieRepository.deleteById(id);


            return "The Movie: '"+existingMovieRecord.getName()+"' was deleted successfully.";
        }catch(Exception e){
            throw new RuntimeException("Error in deleting the movie.", e);
        }
    }
}
