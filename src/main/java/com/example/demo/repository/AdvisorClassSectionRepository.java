package com.example.demo.repository;

import com.example.demo.entity.AdvisorClassSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdvisorClassSectionRepository extends JpaRepository<AdvisorClassSection, UUID> {
    
    // Bản ghi đang phụ trách (end_date is null)
    Optional<AdvisorClassSection> findByStudentClass_IdAndIsActiveTrueAndEndDateIsNull(UUID classId);

    List<AdvisorClassSection> findByStudentClass_IdOrderByStartDateDesc(UUID classId);

    List<AdvisorClassSection> findByEmployee_IdAndIsActiveTrue(UUID employeeId);
}