package com.imdbclone.imdbclone.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false,name = "year_of_release")
    private Integer yearOfRelease;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String plot;

    @Column(nullable = false,length = 255)
    private String poster;

    @Column(nullable = false)
    private Double ratings;

    @Column(nullable = true,length = 20)
    private String imdbMovieId;

    /* ------------ Relationships ------------ */

    /** Single PRODUCER (N‑1) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id")
    private Person producer;

    /** ACTORS (M‑N via join table movies_actors) */
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "movies_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> actors = new HashSet<>();
}
