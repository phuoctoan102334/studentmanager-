package com.example.demo.repository;

import com.example.demo.entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, UUID> {

    Optional<StudentClass> findByCodeAndDeletedAtIsNull(String code);

    List<StudentClass> findByIsActiveTrueAndDeletedAtIsNull();

    @Query("""
        SELECT sc FROM StudentClass sc
        LEFT JOIN FETCH sc.department d
        LEFT JOIN FETCH sc.major m
        LEFT JOIN FETCH sc.employee e
        WHERE sc.isActive = true
          AND sc.deletedAt IS NULL
          AND (:keyword IS NULL
               OR LOWER(sc.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(sc.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:departmentId IS NULL OR sc.department.id = :departmentId)
          AND (:majorId IS NULL OR sc.major.id = :majorId)
        ORDER BY sc.code
    """)
    List<StudentClass> search(
        @Param("keyword") String keyword,
        @Param("departmentId") UUID departmentId,
        @Param("majorId") UUID majorId
    );

    boolean existsByCode(String code);
}