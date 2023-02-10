package com.gd.prometrics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gd.prometrics.model.Student;

@Repository
public interface IStudentRepository extends JpaRepository<Student, Long> {
}