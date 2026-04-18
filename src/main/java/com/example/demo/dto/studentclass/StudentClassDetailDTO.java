package com.example.demo.dto.studentclass;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StudentClassDetailDTO {
    private UUID id;
    private String code;
    private String name;
    private UUID academicYearId;
    private String academicYear;
    private UUID departmentId;
    private String departmentName;
    private UUID majorId;
    private String majorName;
    private UUID trainingProgramId;
    private String trainingProgramName;
    private UUID employeeId;
    private String employeeName;
    private String advisorName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private UUID createdBy;
}