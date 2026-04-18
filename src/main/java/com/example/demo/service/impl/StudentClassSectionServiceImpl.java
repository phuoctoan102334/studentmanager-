package com.example.demo.service.impl;

import com.example.demo.dto.studentsection.StudentSectionDTO;
import com.example.demo.entity.StudentClassSection;
import com.example.demo.repository.StudentClassSectionRepository;
import com.example.demo.service.StudentClassSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentClassSectionServiceImpl implements StudentClassSectionService {

    private final StudentClassSectionRepository sectionRepo;

    @Override
    public List<StudentSectionDTO> getByClass(UUID classId, String status) {
        return sectionRepo.findByClassAndStatus(classId, status)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private StudentSectionDTO toDTO(StudentClassSection scs) {
        StudentSectionDTO d = new StudentSectionDTO();
        d.setId(scs.getId());
        d.setStudentId(scs.getStudent().getId());
        d.setStudentCode(scs.getStudent().getCode());
        d.setStudentFullName(scs.getStudent().getFullName());
        d.setStatus(scs.getStatus());
        d.setStartDate(scs.getStartDate());
        d.setEndDate(scs.getEndDate());
        return d;
    }
}