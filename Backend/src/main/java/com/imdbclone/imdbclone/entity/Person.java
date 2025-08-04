package com.imdbclone.imdbclone.entity;

import com.imdbclone.imdbclone.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "persons",uniqueConstraints = {@UniqueConstraint(name = "UniqueName", columnNames = {"name"})})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    @EqualsAndHashCode.Include
    private String name;

    @Column(nullable = false,length = 10)
    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    private Gender gender;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private LocalDate dob;

    @Column(columnDefinition = "TEXT",nullable = false)
    @EqualsAndHashCode.Include
    private String bio;

    @Column(nullable = true,length = 20)
    private String imdbPersonId;
    /* ------------ Relationships ------------ */

    /** Movies this person PRODUCED (1‑N) */
    @OneToMany(mappedBy = "producer", fetch = FetchType.LAZY)
    private  Set<Movie> producedMovies = new HashSet<>();

    /** Movies this person ACTED IN (M‑N) */
    @ManyToMany(mappedBy = "actors", fetch = FetchType.LAZY)
    private Set<Movie> actedMovies = new HashSet<>();
}
