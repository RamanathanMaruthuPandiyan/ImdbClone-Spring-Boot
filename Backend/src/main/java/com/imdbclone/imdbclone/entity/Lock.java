package com.imdbclone.imdbclone.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name="lock")
public class Lock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String resourceId;
    @CreatedDate
    private LocalDateTime lockedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id",referencedColumnName = "id")
    private Job job;

    public Lock(String resourceId, Job job) {
        this.resourceId = resourceId;
        this.job = job;
    }
}
