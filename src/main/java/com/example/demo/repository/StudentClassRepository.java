// StudentClassRepository.java
package com.example.demo.repository;

import com.example.demo.entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, UUID> {

    Optional<StudentClass> findByCodeAndDeletedAtIsNull(String code);

    List<StudentClass> findByIsActiveTrueAndDeletedAtIsNull();

    boolean existsByCode(String code);
}