package com.example.demo.dto.student;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class StudentDetailDTO {
    private UUID id;
    private String code;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String personalIdentificationNumber;
    private LocalDate dateOfIssue;
    private String cardPlace;
    private String address;
    private String currentAddress;
    private String status;
    private LocalDate admissionYear;
    private Boolean isActive;
    
    private UUID departmentId;
    private String departmentName;
    private UUID majorId;
    private String majorName;
    private UUID academicYearId;
    private String academicYear;
    private UUID studentClasseId;
    private String studentClassName;
    private UUID trainingProgramId;
    private String trainingProgramName;
}