// dto/student/StudentDetailDTO.java
package com.example.demo.dto.student;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime admissionYear;
    private Boolean isActive;
    // FK hiển thị tên
    private UUID departmentId;
    private String departmentName;
    private UUID majorId;
    private String majorName;
    private UUID academicYearId;
    private String academicYear;
    private UUID studentClasseId;
    private String studentClassName;
}