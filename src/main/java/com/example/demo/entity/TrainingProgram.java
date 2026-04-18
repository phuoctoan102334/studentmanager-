package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "training_programs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TrainingProgram {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "UNIQUEIDENTIFIER")
    private UUID id;

    @Column(name = "program_code", nullable = false, unique = true, length = 50)
    private String programCode;

    @Column(name = "program_name", nullable = false, length = 255)
    private String programName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id", nullable = false, columnDefinition = "UNIQUEIDENTIFIER")
    private Major major;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}