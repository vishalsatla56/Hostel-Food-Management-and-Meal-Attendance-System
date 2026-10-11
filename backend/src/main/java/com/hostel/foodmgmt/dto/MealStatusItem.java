package com.hostel.foodmgmt.dto;

import com.hostel.foodmgmt.model.MealType;

public class MealStatusItem {

    private MealType mealType;
    private String status; // "PRESENT", "ABSENT", "NOT_RECORDED"
    private String recordedTime;

    public MealStatusItem() {
    }

    public MealStatusItem(MealType mealType, String status, String recordedTime) {
        this.mealType = mealType;
        this.status = status;
        this.recordedTime = recordedTime;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(String recordedTime) {
        this.recordedTime = recordedTime;
    }
}
