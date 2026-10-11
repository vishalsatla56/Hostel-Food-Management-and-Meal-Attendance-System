package com.hostel.foodmgmt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "attendances")
@CompoundIndex(name = "student_date_meal_idx", def = "{'studentId': 1, 'date': 1, 'mealType': 1}", unique = true)
public class Attendance {

    @Id
    private String id;

    private String studentId;

    private String studentCode;

    private String studentName;

    private LocalDate date;

    private MealType mealType;

    private AttendanceStatus status;

    private LocalDateTime recordedAt = LocalDateTime.now();

    private String recordedBy;

    public Attendance() {
    }

    public Attendance(String studentId, String studentCode, String studentName, LocalDate date, MealType mealType, AttendanceStatus status, String recordedBy) {
        this.studentId = studentId;
        this.studentCode = studentCode;
        this.studentName = studentName;
        this.date = date;
        this.mealType = mealType;
        this.status = status;
        this.recordedBy = recordedBy;
        this.recordedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }
}
