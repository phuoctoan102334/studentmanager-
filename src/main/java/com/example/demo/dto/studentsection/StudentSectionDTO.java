package com.example.demo.dto.studentsection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSectionDTO {
    private UUID id;
    private UUID studentId;
    private String studentCode;
    private String studentFullName;
    private UUID studentClassId;
    private String studentClassName;
    private String trainingProgramName;
    private String status;
    private String note;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;

    // Constructor phụ cho JPQL Projection
    public StudentSectionDTO(UUID id, UUID studentId, String studentCode, String studentFullName, 
                            UUID studentClassId, String studentClassName, String trainingProgramName, 
                            String status, String note, LocalDateTime startDate, LocalDateTime endDate, 
                            Boolean isActive) {
        this.id = id;
        this.studentId = studentId;
        this.studentCode = studentCode;
        this.studentFullName = studentFullName;
        this.studentClassId = studentClassId;
        this.studentClassName = studentClassName;
        this.trainingProgramName = trainingProgramName;
        this.status = status;
        this.note = note;
        this.startDate = startDate != null ? startDate.toLocalDate() : null;
        this.endDate = endDate != null ? endDate.toLocalDate() : null;
        this.isActive = isActive;
    }
}