package com.example.demo.service;

import com.example.demo.dto.studentclass.*;
import java.util.List;
import java.util.UUID;

public interface StudentClassService {
    List<StudentClassListDTO> getAll();
    StudentClassDetailDTO getById(UUID id);
    StudentClassDetailDTO getByCode(String code);
    StudentClassDetailDTO create(StudentClassSaveDTO dto, UUID createdBy);
    StudentClassDetailDTO update(UUID id, StudentClassSaveDTO dto, UUID updatedBy);
    void delete(UUID id, UUID deletedBy);
    List<StudentClassListDTO> search(String keyword, UUID departmentId, UUID majorId);
}