package com.example.demo.service.impl;

import com.example.demo.dto.studentsection.StudentSectionDTO;
import com.example.demo.repository.StudentClassSectionRepository;
import com.example.demo.service.StudentClassSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentClassSectionServiceImpl implements StudentClassSectionService {

    private final StudentClassSectionRepository sectionRepo;

    @Override
    public List<StudentSectionDTO> getByClass(UUID classId, String status) {
        return sectionRepo.findByClassAndStatus(classId, status);
    }
}