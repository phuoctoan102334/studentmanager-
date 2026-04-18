package com.example.demo.service;

import com.example.demo.dto.student.*;
import com.example.demo.dto.studentsection.StudentTransferDTO;
import java.util.List;
import java.util.UUID;

public interface StudentService {
    List<StudentListDTO> getAll();
    StudentDetailDTO getById(UUID id);
    StudentDetailDTO getByCode(String code);
    StudentDetailDTO create(StudentSaveDTO dto, UUID createdBy);
    StudentDetailDTO update(UUID id, StudentSaveDTO dto, UUID updatedBy);
    void delete(UUID id, UUID deletedBy);
    List<StudentListDTO> search(String keyword, UUID departmentId, UUID majorId, String status, UUID classId);
    void transfer(StudentTransferDTO dto, UUID doneBy);
}