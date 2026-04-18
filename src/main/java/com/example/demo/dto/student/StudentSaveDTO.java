// dto/student/StudentSaveDTO.java
package com.example.demo.dto.student;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StudentSaveDTO {
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
    private LocalDateTime admissionYear;
    private UUID academicYearId;
    private UUID departmentId;
    private UUID majorId;
    private UUID trainingProgramId;
    private UUID studentClasseId;
}