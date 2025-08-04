package com.imdbclone.imdbclone.repository;

import com.imdbclone.imdbclone.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie,UUID>, JpaSpecificationExecutor<Movie> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    @Query("SELECT DISTINCT m.yearOfRelease FROM Movie m ORDER BY m.yearOfRelease DESC")
    List<Integer> findDistinctYearsOfRelease();

    @Query("SELECT DISTINCT m.ratings FROM Movie m ORDER BY m.ratings DESC")
    List<Double> findDistinctRatings();

}
