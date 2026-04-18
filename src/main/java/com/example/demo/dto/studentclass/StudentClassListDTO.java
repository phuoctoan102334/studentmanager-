// dto/studentclasse/StudentClassListDTO.java
package com.example.demo.dto.studentclasse;

import lombok.Data;
import java.util.UUID;

@Data
public class StudentClassListDTO {
    private UUID id;
    private String code;
    private String name;
    private String departmentName;
    private String advisorName;
    private Boolean isActive;
}