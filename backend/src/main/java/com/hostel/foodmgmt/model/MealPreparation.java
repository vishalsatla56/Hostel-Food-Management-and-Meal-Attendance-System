package com.hostel.foodmgmt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "meal_preparations")
@CompoundIndex(name = "date_meal_idx", def = "{'date': 1, 'mealType': 1}", unique = true)
public class MealPreparation {

    @Id
    private String id;

    private LocalDate date;

    private MealType mealType;

    private int mealsPrepared;

    private String notes;

    private LocalDateTime updatedAt = LocalDateTime.now();

    public MealPreparation() {
    }

    public MealPreparation(LocalDate date, MealType mealType, int mealsPrepared, String notes) {
        this.date = date;
        this.mealType = mealType;
        this.mealsPrepared = mealsPrepared;
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
