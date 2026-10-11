package com.hostel.foodmgmt.repository;

import com.hostel.foodmgmt.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByStudentCode(String studentCode);
    List<Student> findByActiveTrue();
    long countByActiveTrue();
}
