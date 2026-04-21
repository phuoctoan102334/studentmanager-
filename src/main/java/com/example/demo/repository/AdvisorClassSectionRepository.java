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
    @org.springframework.data.jpa.repository.Query("SELECT a FROM AdvisorClassSection a JOIN FETCH a.employee JOIN FETCH a.studentClass WHERE a.studentClass.id = :classId AND a.isActive = true AND a.endDate IS NULL")
    Optional<AdvisorClassSection> findByStudentClass_IdAndIsActiveTrueAndEndDateIsNull(UUID classId);

    @org.springframework.data.jpa.repository.Query("SELECT a FROM AdvisorClassSection a JOIN FETCH a.employee JOIN FETCH a.studentClass WHERE a.studentClass.id = :classId ORDER BY a.startDate DESC")
    List<AdvisorClassSection> findByStudentClass_IdOrderByStartDateDesc(UUID classId);

    @org.springframework.data.jpa.repository.Query("SELECT a FROM AdvisorClassSection a JOIN FETCH a.employee JOIN FETCH a.studentClass WHERE a.employee.id = :employeeId AND a.isActive = true")
    List<AdvisorClassSection> findByEmployee_IdAndIsActiveTrue(UUID employeeId);

    @org.springframework.data.jpa.repository.Query("SELECT a FROM AdvisorClassSection a JOIN FETCH a.employee JOIN FETCH a.studentClass WHERE a.deletedAt IS NULL")
    List<AdvisorClassSection> findAllByDeletedAtIsNull();
}