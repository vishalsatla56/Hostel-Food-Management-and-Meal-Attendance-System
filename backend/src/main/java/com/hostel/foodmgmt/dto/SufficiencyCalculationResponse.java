package com.hostel.foodmgmt.dto;

import com.hostel.foodmgmt.model.MealType;
import com.hostel.foodmgmt.model.SufficiencyStatus;

import java.time.LocalDate;

public class SufficiencyCalculationResponse {

    private LocalDate date;
    private MealType mealType;
    private int mealsPrepared;
    private int actualAttendance;
    private int expectedDemand;
    private int difference;
    private SufficiencyStatus status;
    private int statusQuantity;
    private String statusMessage;
    private String calculationMethod;
    private String baselineNote;

    private boolean preparationRecordExists;
    private String notes;

    // Optional Historical Prediction
    private boolean hasHistoricalData;
    private int historicalRecordCount;
    private Double historicalAverageDemand;
    private Integer historicalDifference;
    private SufficiencyStatus historicalStatus;
    private String historicalStatusMessage;
    private String historicalMethod;

    public SufficiencyCalculationResponse() {
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

    public int getActualAttendance() {
        return actualAttendance;
    }

    public void setActualAttendance(int actualAttendance) {
        this.actualAttendance = actualAttendance;
    }

    public int getExpectedDemand() {
        return expectedDemand;
    }

    public void setExpectedDemand(int expectedDemand) {
        this.expectedDemand = expectedDemand;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

    public SufficiencyStatus getStatus() {
        return status;
    }

    public void setStatus(SufficiencyStatus status) {
        this.status = status;
    }

    public int getStatusQuantity() {
        return statusQuantity;
    }

    public void setStatusQuantity(int statusQuantity) {
        this.statusQuantity = statusQuantity;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    public String getBaselineNote() {
        return baselineNote;
    }

    public void setBaselineNote(String baselineNote) {
        this.baselineNote = baselineNote;
    }

    public boolean isPreparationRecordExists() {
        return preparationRecordExists;
    }

    public void setPreparationRecordExists(boolean preparationRecordExists) {
        this.preparationRecordExists = preparationRecordExists;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isHasHistoricalData() {
        return hasHistoricalData;
    }

    public void setHasHistoricalData(boolean hasHistoricalData) {
        this.hasHistoricalData = hasHistoricalData;
    }

    public int getHistoricalRecordCount() {
        return historicalRecordCount;
    }

    public void setHistoricalRecordCount(int historicalRecordCount) {
        this.historicalRecordCount = historicalRecordCount;
    }

    public Double getHistoricalAverageDemand() {
        return historicalAverageDemand;
    }

    public void setHistoricalAverageDemand(Double historicalAverageDemand) {
        this.historicalAverageDemand = historicalAverageDemand;
    }

    public Integer getHistoricalDifference() {
        return historicalDifference;
    }

    public void setHistoricalDifference(Integer historicalDifference) {
        this.historicalDifference = historicalDifference;
    }

    public SufficiencyStatus getHistoricalStatus() {
        return historicalStatus;
    }

    public void setHistoricalStatus(SufficiencyStatus historicalStatus) {
        this.historicalStatus = historicalStatus;
    }

    public String getHistoricalStatusMessage() {
        return historicalStatusMessage;
    }

    public void setHistoricalStatusMessage(String historicalStatusMessage) {
        this.historicalStatusMessage = historicalStatusMessage;
    }

    public String getHistoricalMethod() {
        return historicalMethod;
    }

    public void setHistoricalMethod(String historicalMethod) {
        this.historicalMethod = historicalMethod;
    }
}
