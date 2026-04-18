// dto/studentclasse/StudentClassDetailDTO.java
package com.example.demo.dto.studentclasse;

import lombok.Data;
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
    private String advisorName;
    private Boolean isActive;
}