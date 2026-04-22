package com.example.demo.dto.studentclass;

import lombok.Data;
import java.util.UUID;

@Data
public class StudentClassListDTO {
    private UUID id;
    private String code;
    private String name;
    private String departmentName;
    private String majorName;
    private String academicYearName;
    private String advisorName;
    private Boolean isActive;
}