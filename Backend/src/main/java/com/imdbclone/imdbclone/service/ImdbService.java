package com.imdbclone.imdbclone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.imdbclone.imdbclone.component.RapidApiProperties;
import com.imdbclone.imdbclone.dto.ImdbImportDto;
import com.imdbclone.imdbclone.entity.Job;
import com.imdbclone.imdbclone.entity.Lock;
import com.imdbclone.imdbclone.entity.Movie;
import com.imdbclone.imdbclone.entity.Person;
import com.imdbclone.imdbclone.enums.Gender;
import com.imdbclone.imdbclone.enums.JobName;
import com.imdbclone.imdbclone.enums.JobStatus;
import com.imdbclone.imdbclone.exceptions.BadRequestException;
import com.imdbclone.imdbclone.exceptions.ConflictException;
import com.imdbclone.imdbclone.exceptions.ExternalApiException;
import com.imdbclone.imdbclone.mapper.Mapper;
import com.imdbclone.imdbclone.objects.ImdbPerson;
import com.imdbclone.imdbclone.objects.ImdbSearchRequest;
import com.imdbclone.imdbclone.objects.ImdbSearchResponse;
import com.imdbclone.imdbclone.repository.JobRepository;
import com.imdbclone.imdbclone.repository.LockRepository;
import com.imdbclone.imdbclone.repository.MovieRepository;
import com.imdbclone.imdbclone.repository.PersonRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImdbService {
    private final RapidApiProperties rapidApiProperties;

    private final JobRepository jobRepository;

    private final LockRepository lockRepository;

    private final PersonRepository personRepository;

    private final MovieRepository movieRepository;

    public ImdbService(RapidApiProperties rapidApiProperties, JobService jobService, JobRepository jobRepository, LockRepository lockRepository, PersonRepository personRepository, MovieRepository movieRepository) {
        this.rapidApiProperties = rapidApiProperties;
        this.jobRepository = jobRepository;
        this.lockRepository = lockRepository;
        this.personRepository = personRepository;
        this.movieRepository = movieRepository;
    }

    public Flux<ImdbSearchResponse> getSearch(ImdbSearchRequest imdbSearchRequest) {
        if (imdbSearchRequest.getSearch() == null || imdbSearchRequest.getSearch().isBlank()) {
            throw new BadRequestException("Search field can't be empty.");
        }

        Set<String> resourceIdList = lockRepository.findDistinctResourceIds();

        return WebClient.builder().build().get()
                .uri(rapidApiProperties.getSearchMovie()+
                        "?searchTerm="+imdbSearchRequest.getSearch()+
                        "&type="+imdbSearchRequest.getType()+
                        "&limit="+imdbSearchRequest.getLimit()+
                        "&language="+imdbSearchRequest.getLanguage()
                )
                .header("x-rapidapi-key", rapidApiProperties.getAccessKey())
                .header("x-rapidapi-host", rapidApiProperties.getHost())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMapMany(jsonNode -> {
                    JsonNode edges = jsonNode.at("/data/mainSearch/edges");

                    if (edges.isMissingNode() || !edges.isArray()) {
                        return Flux.error(new RuntimeException("Failed to fetch movies from IMDb server."));
                    }

                    return Flux.fromIterable(edges)
                            .map(edge -> {
                                JsonNode node = edge.path("node");
                                JsonNode entity = node.path("entity");

                                String imdbId = entity.path("id").asText("N/A");
                                String name = entity.path("titleText").path("text").asText("Unknown Title");
                                String year = entity.path("releaseYear").path("year").asText("Unknown Year");
                                String poster = entity.path("primaryImage").path("url").asText("No Image Available");

                                List<ImdbPerson> actors = new ArrayList<>();
                                JsonNode credits = entity.at("/principalCredits/0/credits");

                                if (credits.isArray()) {
                                    for (JsonNode credit : credits) {
                                        String personId = credit.path("name").path("id").asText();
                                        String personName = credit.path("name").path("nameText").path("text").asText();

                                        if (!personId.isEmpty() && !personName.isEmpty()) {
                                            actors.add(new ImdbPerson(personId, personName));
                                        }
                                    }
                                }
                                boolean isImportAllowed = resourceIdList.contains(imdbId);
                                return ImdbSearchResponse.builder()
                                        .imdbId(imdbId)
                                        .name(name)
                                        .yearOfRelease(year)
                                        .poster(poster)
                                        .actors(actors)
                                        .isImportAllowed(!isImportAllowed)
                                        .build();
                            });
                })
                .onErrorResume(e -> {
                    return Flux.error(new ExternalApiException("Unable to fetch data from IMDb Server. Please try " +
                            "again later."));
                });
    }

    @Transactional
    public void insertIntoTheDB(Movie movie, Job job,UUID lockId) {
        try {
            movieRepository.save(movie);

            job.setStatus(JobStatus.COMPLETED);
            job.setCompletionPercentage(100);
            job.setMessage("The movie '" + movie.getName() + "' was imported, and the process completed successfully.");
            jobRepository.save(job);

        } catch (Exception e) {
            job.setStatus(JobStatus.ERRORED);
            job.setCompletionPercentage(0);
            job.setReason(e.getMessage());
            job.setEnded(LocalDateTime.now());
            jobRepository.save(job);
            if(lockId!=null){
                lockRepository.deleteById(lockId);
            }
        }
    }

    public void getActors(List<Person> personList, List<ImdbPerson> actors, Job job,UUID lockId) {
        for (ImdbPerson imdbActor : actors) {
            try {
                JsonNode actorNode = WebClient.builder().build().get()
                        .uri(rapidApiProperties.getGetOverview() + "?nconst=" + imdbActor.getPersonId())
                        .header("x-rapidapi-key", rapidApiProperties.getAccessKey())
                        .header("x-rapidapi-host", rapidApiProperties.getHost())
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .block(); // synchronous fetch

                if (actorNode == null || actorNode.path("data").isMissingNode()) {
                    continue;
                }

                JsonNode nameNode = actorNode.path("data").path("name");

                Person person = new Person();

                person.setName(nameNode.path("nameText").path("text").asText());

                // Parse DOB safely
                String dobString = nameNode.path("birthDate").path("date").asText(null);
                if (dobString != null) {
                    person.setDob(LocalDate.parse(dobString));
                } else {
                    person.setDob(LocalDate.of(1970, 1, 1)); // fallback or handle null appropriately
                }

                // Set gender (optional – you can derive this from somewhere or skip if not available)
                person.setGender(Gender.MALE); // default or logic if you have gender info elsewhere

                // Bio
                String bio = nameNode.path("bio").path("text").path("plainText").asText("");
                person.setBio(bio);

                // IMDb Person ID
                person.setImdbPersonId(imdbActor.getPersonId());

                // Add to list
                personList.add(person);

                // Job progress update (optional granularity)
                job.setCompletionPercentage(job.getCompletionPercentage() + 1);
                jobRepository.save(job);

            } catch (Exception e) {
                // Handle each actor's error without failing the entire batch
                job.setStatus(JobStatus.ERRORED);
                job.setCompletionPercentage(0);
                job.setReason(e.getMessage());
                job.setEnded(LocalDateTime.now());
                jobRepository.save(job);
                if(lockId!=null){
                    lockRepository.deleteById(lockId);
                }
            }
        }
    }

    public void getRatingsAndPlot(Movie movie,Job job,UUID lockId){
        try {
            job.setStatus(JobStatus.IN_PROGRESS);
            job.setCompletionPercentage(15);
            jobRepository.save(job);
            // --- Get Ratings ---
            JsonNode ratingNode = WebClient.builder().build().get()
                    .uri(rapidApiProperties.getGetRatings() + "?tconst=" + movie.getImdbMovieId())
                    .header("x-rapidapi-key", rapidApiProperties.getAccessKey())
                    .header("x-rapidapi-host", rapidApiProperties.getHost())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();  // <- BLOCKING here

            if (ratingNode != null) {
                JsonNode titleNode = ratingNode.path("data").path("title");
                double rating = titleNode.path("ratingsSummary").path("aggregateRating").asDouble(4.0);
                movie.setRatings(rating);

                job.setStatus(JobStatus.IN_PROGRESS);
                job.setCompletionPercentage(30);
                jobRepository.save(job);
            }

            // --- Get Plot ---
            JsonNode plotNode = WebClient.builder().build().get()
                    .uri(rapidApiProperties.getGetPlot() + "?tconst=" + movie.getImdbMovieId())
                    .header("x-rapidapi-key", rapidApiProperties.getAccessKey())
                    .header("x-rapidapi-host", rapidApiProperties.getHost())
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();  // <- BLOCKING again

            if (plotNode != null) {
                JsonNode titleNode = plotNode.path("data").path("title");
                String plot = titleNode.path("plot").path("plotText").path("plainText").asText("");
                movie.setPlot(plot);

                job.setStatus(JobStatus.IN_PROGRESS);
                job.setCompletionPercentage(50);
                jobRepository.save(job);
            }

        } catch (Exception e) {
            job.setStatus(JobStatus.ERRORED);
            job.setCompletionPercentage(0);
            job.setReason(e.getMessage());
            job.setEnded(LocalDateTime.now());
            jobRepository.save(job);
            if(lockId!=null){
                lockRepository.deleteById(lockId);
            }
        }
    }

    @Async
    public void importFromImdb(ImdbImportDto imdbImportDto) {
        Job job = new Job(JobName.IMPORT_IMDB,
                imdbImportDto.getImdbId(),
                JobStatus.NOT_STARTED,
                null,
                null,
                0,
                null,
                null
                );
        jobRepository.save(job);
        Lock lock = new Lock(imdbImportDto.getImdbId(),job);
        UUID lockId = null;
        try{
            boolean isLockExist = lockRepository.existsByResourceId(imdbImportDto.getImdbId());
            if(isLockExist){
                throw new ConflictException("The movie " +imdbImportDto.getName()+ " has already been imported or is " +
                        "currently in the " +
                        "process of" +
                        " being imported.");
            }

            lockId = lockRepository.save(lock).getId();

            job.setStatus(JobStatus.IN_PROGRESS);
            job.setCompletionPercentage(10);
            job.setStarted(LocalDateTime.now());
            jobRepository.save(job);

            Movie movie = Mapper.fromImdbImportDto(imdbImportDto);

            getRatingsAndPlot(movie,job,lockId);

            job.setStatus(JobStatus.IN_PROGRESS);
            job.setCompletionPercentage(55);
            jobRepository.save(job);

            List<Person> personList = new ArrayList<>();
            getActors(personList,imdbImportDto.getActors(),job,lockId);

            job.setStatus(JobStatus.IN_PROGRESS);
            job.setCompletionPercentage(60);
            jobRepository.save(job);


            List<Person> existingPersons = personRepository.findAllByNameInAndDobIn(
                    personList.stream().map(Person::getName).distinct().toList(),
                    personList.stream().map(Person::getDob).distinct().toList()
            );

            Set<String> existingKeys = existingPersons.stream()
                    .map(p -> p.getName() + "::" + p.getDob())
                    .collect(Collectors.toSet());

            Set<Person> uniquePersonsToInsert = personList.stream()
                    .filter(p -> !existingKeys.contains(p.getName() + "::" + p.getDob()))
                    .collect(Collectors.toSet());

            job.setStatus(JobStatus.IN_PROGRESS);
            job.setCompletionPercentage(65);
            jobRepository.save(job);

            movie.setActors(uniquePersonsToInsert);

            insertIntoTheDB(movie,job,lockId);

        }catch(Exception e){
            job.setStatus(JobStatus.ERRORED);
            job.setCompletionPercentage(0);
            job.setReason(e.getMessage());
            job.setEnded(LocalDateTime.now());
            jobRepository.save(job);
            if(lockId!=null){
                lockRepository.deleteById(lockId);
            }
        }
    }
}
