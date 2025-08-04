package com.imdbclone.imdbclone.entity;

import com.imdbclone.imdbclone.enums.JobName;
import com.imdbclone.imdbclone.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false,length = 20)
    @Enumerated(EnumType.STRING)
    private JobName name;
    @Column(nullable = false,length = 10)
    private String imdbId;
    @Column(nullable = false,length = 20)
    @Enumerated(EnumType.STRING)
    private JobStatus status;
    @CreatedDate
    private LocalDateTime created;
    private LocalDateTime started;
    private LocalDateTime ended;
    @Column(nullable = false)
    private Integer completionPercentage;
    private String reason;
    private String message;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "job")
    private Lock lock;

    public Job(JobName name,
               String imdbId,
               JobStatus status,
               LocalDateTime started,
               LocalDateTime ended,
               Integer completionPercentage,
               String reason,
               String message) {
        this.name = name;
        this.imdbId = imdbId;
        this.status = status;
        this.started = started;
        this.ended = ended;
        this.completionPercentage = completionPercentage;
        this.reason = reason;
        this.message = message;
    }
}
