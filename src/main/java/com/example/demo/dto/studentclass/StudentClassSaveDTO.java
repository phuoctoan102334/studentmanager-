package com.example.demo.dto.studentclass;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter
public class StudentClassSaveDTO {
    private String code;
    private String name;
    private UUID academicYearId;
    private UUID departmentId;
    private UUID majorId;
    private UUID trainingProgramId;
    private UUID employeeId;
}