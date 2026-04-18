package com.example.demo.controller;

import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/discovery")
@RequiredArgsConstructor
public class DiscoveryController {

    private final DepartmentRepository departmentRepo;
    private final MajorRepository majorRepo;
    private final AcademicYearRepository academicYearRepo;
    private final StudentClassRepository classRepo;
    private final TrainingProgramRepository trainingProgramRepo;
    private final EmployeeRepository employeeRepo;

    @GetMapping("/options")
    public Map<String, List<?>> getOptions() {
        return Map.of(
            "departments", departmentRepo.findAll().stream()
                .map(d -> Map.of("id", d.getId(), "name", d.getName())).collect(Collectors.toList()),
            "majors", majorRepo.findAll().stream()
                .map(m -> Map.of("id", m.getId(), "name", m.getMajorName())).collect(Collectors.toList()),
            "academicYears", academicYearRepo.findAll().stream()
                .map(a -> Map.of("id", a.getId(), "name", a.getAcademicYear())).collect(Collectors.toList()),
            "classes", classRepo.findByIsActiveTrueAndDeletedAtIsNull().stream()
                .map(c -> Map.of("id", c.getId(), "name", c.getName(), "code", c.getCode())).collect(Collectors.toList()),
            "programs", trainingProgramRepo.findAll().stream()
                .map(p -> Map.of("id", p.getId(), "name", p.getProgramName())).collect(Collectors.toList()),
            "employees", employeeRepo.findAll().stream()
                .map(e -> Map.of("id", e.getId(), "name", e.getFullName())).collect(Collectors.toList())
        );
    }
}