package com.imdbclone.imdbclone.service;

import com.imdbclone.imdbclone.dto.*;
import com.imdbclone.imdbclone.entity.Person;
import com.imdbclone.imdbclone.enums.ActionItems;
import com.imdbclone.imdbclone.enums.Gender;
import com.imdbclone.imdbclone.exceptions.BadRequestException;
import com.imdbclone.imdbclone.exceptions.ConflictException;
import com.imdbclone.imdbclone.exceptions.InternalServerErrorException;
import com.imdbclone.imdbclone.exceptions.ResourceNotFoundException;
import com.imdbclone.imdbclone.mapper.Mapper;
import com.imdbclone.imdbclone.objects.FilterOption;
import com.imdbclone.imdbclone.objects.PersonFilter;
import com.imdbclone.imdbclone.repository.PersonRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    public PaginationDto<PersonSummaryDto> pagination(String search, PersonFilter filter, Pageable pageable) {
        Specification<Person> spec = (root, q, cb)->{
            List<Predicate> predicates=new ArrayList<>();

            if(search!=null && !search.isBlank()){
                predicates.add( cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"));
            }

            if (filter.getGender() != null && !filter.getGender().isEmpty()) {
                predicates.add( root.get("gender").in(filter.getGender()));
            }

            if (filter.getDob() != null) {
                predicates.add( root.get("dob").in(filter.getDob()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Person> page = personRepository.findAll(spec,pageable);
        return Mapper.toPaginationDto(page.map(Mapper::toPersonSummaryDto));
    }

    public PersonDto getPerson(UUID id){
        Optional<Person> person = personRepository.findById(id);
        if(person.isEmpty()){
            throw new ResourceNotFoundException("Received invalid id.");
        }
        return Mapper.toPersonDto(person.get());
    }
    public Map<String, FilterOption<?>> getFilterOptions() {
        List<String> distinctGender = personRepository.findDistinctGenders().stream().map(Gender::getLabel).toList();

        Map<String, FilterOption<?>> filterOptions = Map.of(
                "gender",new FilterOption<>("Gender",distinctGender)
        );

        return filterOptions;
    }

    public List<String> getActionItems(UUID id) {
        Person personRecordExist = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person doesn't exists."));


        boolean isProducer = !personRecordExist.getProducedMovies().isEmpty();
        boolean isActor = !personRecordExist.getActedMovies().isEmpty();
        List<String> actionItems = new ArrayList<>();

        actionItems.add(ActionItems.EDIT.getCode());

        if (!(isProducer || isActor)) {
            actionItems.add(ActionItems.DELETE.getCode());
        }

        return  actionItems;
    }

    public String addPerson(PersonDto personDto){
        Person person = Mapper.fromPersonDto(personDto);

        boolean isNameExist = personRepository.existsByName(person.getName());
        if (isNameExist) {
            throw new ConflictException("Person with name '" + person.getName() + "' already exists.");
        }

        Person resultEntity = personRepository.save(person);

        if (resultEntity.getId() == null) {
            throw new InternalServerErrorException("Error while saving the person '" + person.getName() + "'.");
        }

        return "The Person: '" + person.getName() + "' was added successfully.";
    }

    public String updatePerson(UUID id, PersonDto personDto) {
        Person existingPersonRecord = personRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(
                "Person doesn't exists."));

        personDto.setId(id);
        Person person = Mapper.fromPersonDto(personDto);

        boolean isNameExist = personRepository.existsByNameAndIdNot(person.getName(),id);
        if (isNameExist) {
            throw new ConflictException("Person with name '" + person.getName() + "' already exists.");
        }

        boolean isUnChanged = existingPersonRecord.equals(person);

        if (isUnChanged){
            throw new BadRequestException("No modifications found.");
        }

        Person resultEntity = personRepository.save(person);

        if(resultEntity.getId()==null){
            throw new InternalServerErrorException("Error while updating the person '" +person.getName()+ "'.");
        }
        return "The Person: '" +person.getName()+ "' was updated successfully.";
    }

    public String deletePerson(UUID id) {
        Person personRecordExist = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person doesn't exists."));


        boolean isProducer = !personRecordExist.getProducedMovies().isEmpty();
        boolean isActor = !personRecordExist.getActedMovies().isEmpty();

        if (isProducer || isActor) {
            throw new IllegalStateException("Cannot delete person because they are linked to one or more movies.");
        }

        personRepository.deleteById(id);

        return "The Person: '"+personRecordExist.getName()+"' was deleted successfully.";
    }
}
