package com.example.demo.dto.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class StudentDetailDTO {
    private UUID id;
    private String code;
    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String gender;
    private String personalIdentificationNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfIssue;
    private String cardPlace;
    private String address;
    private String currentAddress;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
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