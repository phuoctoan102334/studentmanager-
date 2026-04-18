// AdvisorClassSectionRepository.java
package com.example.demo.repository;

import com.example.demo.entity.AdvisorClassSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdvisorClassSectionRepository extends JpaRepository<AdvisorClassSection, UUID> {

    // Phân công đang active (chưa kết thúc)
    Optional<AdvisorClassSection> findByStudentClass_IdAndIsActiveTrueAndEndDateIsNull(UUID classId);

    List<AdvisorClassSection> findByEmployee_IdOrderByStartDateDesc(UUID employeeId);

    List<AdvisorClassSection> findByIsActiveTrueAndEndDateIsNull();
}