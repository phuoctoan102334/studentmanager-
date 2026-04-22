package com.example.demo.service;

import com.example.demo.dto.studentclass.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface StudentClassService {
    List<StudentClassListDTO> getAll();
    Page<StudentClassListDTO> getAllPaged(Pageable pageable);
    StudentClassDetailDTO getById(UUID id);
    StudentClassDetailDTO getByCode(String code);
    StudentClassDetailDTO create(StudentClassSaveDTO dto, UUID createdBy);
    StudentClassDetailDTO update(UUID id, StudentClassSaveDTO dto, UUID updatedBy);
    void delete(UUID id, UUID deletedBy);
    List<StudentClassListDTO> search(String keyword, UUID departmentId, UUID majorId);
    Page<StudentClassListDTO> searchPaged(String keyword, UUID departmentId, UUID majorId, Pageable pageable);
}