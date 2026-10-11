package com.hostel.foodmgmt.dto;

import com.hostel.foodmgmt.model.MealType;

import java.time.LocalDate;
import java.util.List;

public class AttendanceListResponse {

    private LocalDate date;
    private MealType mealType;
    private long totalPresentCount;
    private long totalAbsentCount;
    private List<AttendanceRecordDto> records;

    public AttendanceListResponse() {
    }

    public AttendanceListResponse(LocalDate date, MealType mealType, long totalPresentCount, long totalAbsentCount, List<AttendanceRecordDto> records) {
        this.date = date;
        this.mealType = mealType;
        this.totalPresentCount = totalPresentCount;
        this.totalAbsentCount = totalAbsentCount;
        this.records = records;
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

    public long getTotalPresentCount() {
        return totalPresentCount;
    }

    public void setTotalPresentCount(long totalPresentCount) {
        this.totalPresentCount = totalPresentCount;
    }

    public long getTotalAbsentCount() {
        return totalAbsentCount;
    }

    public void setTotalAbsentCount(long totalAbsentCount) {
        this.totalAbsentCount = totalAbsentCount;
    }

    public List<AttendanceRecordDto> getRecords() {
        return records;
    }

    public void setRecords(List<AttendanceRecordDto> records) {
        this.records = records;
    }
}
