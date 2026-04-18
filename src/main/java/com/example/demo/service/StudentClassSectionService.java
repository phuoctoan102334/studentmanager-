package com.example.demo.service;

import com.example.demo.dto.studentsection.StudentSectionDTO;
import java.util.List;
import java.util.UUID;

public interface StudentClassSectionService {
    List<StudentSectionDTO> getByClass(UUID classId, String status);
}