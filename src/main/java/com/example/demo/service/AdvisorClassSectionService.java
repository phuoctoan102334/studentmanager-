package com.example.demo.service;

import com.example.demo.dto.studentsection.StudentSectionDTO;
import java.util.List;
import java.util.UUID;

public interface AdvisorClassSectionService {
    List<StudentSectionDTO> getActiveAdvisors();
    void endAdvisorAssignment(UUID assignmentId, UUID doneBy);
}