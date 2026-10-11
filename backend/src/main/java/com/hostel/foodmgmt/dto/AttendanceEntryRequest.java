package com.hostel.foodmgmt.dto;

import com.hostel.foodmgmt.model.AttendanceStatus;
import com.hostel.foodmgmt.model.MealType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AttendanceEntryRequest {

    @NotBlank(message = "Student is required")
    private String studentId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Meal type is required")
    private MealType mealType;

    @NotNull(message = "Status is required")
    private AttendanceStatus status;

    public AttendanceEntryRequest() {
    }

    public AttendanceEntryRequest(String studentId, LocalDate date, MealType mealType, AttendanceStatus status) {
        this.studentId = studentId;
        this.date = date;
        this.mealType = mealType;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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
}
