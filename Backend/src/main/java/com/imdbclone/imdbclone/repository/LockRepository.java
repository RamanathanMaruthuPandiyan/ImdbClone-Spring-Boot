package com.imdbclone.imdbclone.repository;

import com.imdbclone.imdbclone.entity.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface LockRepository extends JpaRepository<Lock, UUID> {
    boolean existsByResourceId(String imdbId);

    Optional<Lock> findByResourceId(String imdbMovieId);

    @Query("SELECT DISTINCT l.resourceId FROM Lock l ORDER BY l.resourceId DESC")
    Set<String> findDistinctResourceIds();
}
