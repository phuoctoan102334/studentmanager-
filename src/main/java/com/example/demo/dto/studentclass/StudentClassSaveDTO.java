package com.example.demo.dto.studentclass;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter
public class StudentClassSaveDTO {
    @NotBlank(message = "Mã lớp không được để trống")
    private String code;
    
    @NotBlank(message = "Tên lớp không được để trống")
    private String name;
    
    private UUID academicYearId;
    private UUID departmentId;
    private UUID majorId;
    private UUID trainingProgramId;
    private UUID employeeId;
}