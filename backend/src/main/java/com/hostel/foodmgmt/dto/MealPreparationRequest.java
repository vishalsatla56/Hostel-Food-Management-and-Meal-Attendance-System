package com.hostel.foodmgmt.dto;

import com.hostel.foodmgmt.model.MealType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class MealPreparationRequest {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Meal type is required")
    private MealType mealType;

    @Min(value = 0, message = "Meals prepared cannot be negative")
    private int mealsPrepared;

    private String notes;

    public MealPreparationRequest() {
    }

    public MealPreparationRequest(LocalDate date, MealType mealType, int mealsPrepared, String notes) {
        this.date = date;
        this.mealType = mealType;
        this.mealsPrepared = mealsPrepared;
        this.notes = notes;
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

    public int getMealsPrepared() {
        return mealsPrepared;
    }

    public void setMealsPrepared(int mealsPrepared) {
        this.mealsPrepared = mealsPrepared;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
