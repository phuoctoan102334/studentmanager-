package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "academic_years")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AcademicYear {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "UNIQUEIDENTIFIER")
    private UUID id;

    @Column(name = "academic_code", nullable = false, unique = true, length = 50)
    private String academicCode;

    @Column(name = "academic_name", length = 100)
    private String academicName;

    @Column(name = "academic_year", length = 20)
    private String academicYear;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}