package com.imdbclone.imdbclone.repository;

import com.imdbclone.imdbclone.entity.Person;
import com.imdbclone.imdbclone.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID>, JpaSpecificationExecutor<Person> {
    @Query("SELECT DISTINCT p.gender FROM Person p")
    List<Gender> findDistinctGenders();

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name,UUID id);

    List<Person> findAllByNameInAndDobIn(List<String> list, List<LocalDate> list1);
}
