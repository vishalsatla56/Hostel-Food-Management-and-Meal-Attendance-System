package com.hostel.foodmgmt.repository;

import com.hostel.foodmgmt.model.Attendance;
import com.hostel.foodmgmt.model.AttendanceStatus;
import com.hostel.foodmgmt.model.MealType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    Optional<Attendance> findByStudentIdAndDateAndMealType(String studentId, LocalDate date, MealType mealType);

    List<Attendance> findByStudentIdOrderByDateDescRecordedAtDesc(String studentId);

    List<Attendance> findByStudentIdAndDateBetweenOrderByDateDescRecordedAtDesc(String studentId, LocalDate startDate, LocalDate endDate);

    List<Attendance> findByStudentIdAndMealTypeOrderByDateDescRecordedAtDesc(String studentId, MealType mealType);

    List<Attendance> findByStudentIdAndDateBetweenAndMealTypeOrderByDateDescRecordedAtDesc(
            String studentId, LocalDate startDate, LocalDate endDate, MealType mealType);

    long countByStudentIdAndStatus(String studentId, AttendanceStatus status);

    long countByStudentIdAndMealTypeAndStatus(String studentId, MealType mealType, AttendanceStatus status);

    long countByStudentIdAndDateBetweenAndStatus(String studentId, LocalDate startDate, LocalDate endDate, AttendanceStatus status);

    List<Attendance> findByDateAndMealType(LocalDate date, MealType mealType);

    long countByDateAndMealTypeAndStatus(LocalDate date, MealType mealType, AttendanceStatus status);

    long countByDateAndStatus(LocalDate date, AttendanceStatus status);

    long countByStatus(AttendanceStatus status);

    List<Attendance> findByMealTypeAndDateBeforeAndStatus(MealType mealType, LocalDate date, AttendanceStatus status);

    List<Attendance> findByStudentIdAndDate(String studentId, LocalDate date);
}
