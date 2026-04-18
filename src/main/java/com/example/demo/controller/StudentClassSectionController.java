package com.example.demo.controller;

import com.example.demo.dto.studentsection.StudentSectionDTO;
import com.example.demo.service.StudentClassSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/student-sections")
@RequiredArgsConstructor
public class StudentClassSectionController {

    private final StudentClassSectionService sectionService;

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<StudentSectionDTO>> getByClass(
            @PathVariable UUID classId,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(sectionService.getByClass(classId, status));
    }
}