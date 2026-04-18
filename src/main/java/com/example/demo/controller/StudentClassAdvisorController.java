package com.example.demo.controller;

import com.example.demo.dto.advisorsection.*;
import com.example.demo.service.AdvisorClassSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/advisor-sections")
@RequiredArgsConstructor
public class StudentClassAdvisorController {

    private final AdvisorClassSectionService advisorService;

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<AdvisorSectionListDTO>> getByClass(@PathVariable UUID classId) {
        return ResponseEntity.ok(advisorService.getByClass(classId));
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assign(@RequestBody AdvisorSectionSaveDTO dto) {
        advisorService.assignAdvisor(dto, null);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/end/{id}")
    public ResponseEntity<Void> endAssignment(@PathVariable UUID id) {
        advisorService.endAssignment(id, null);
        return ResponseEntity.ok().build();
    }
}