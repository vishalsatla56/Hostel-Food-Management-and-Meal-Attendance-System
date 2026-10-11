package com.hostel.foodmgmt.dto;

import java.time.LocalDate;

public class DateStatsDto {

    private LocalDate date;
    private long breakfastCount;
    private long lunchCount;
    private long dinnerCount;
    private long totalAttendance;

    public DateStatsDto() {
    }

    public DateStatsDto(LocalDate date, long breakfastCount, long lunchCount, long dinnerCount, long totalAttendance) {
        this.date = date;
        this.breakfastCount = breakfastCount;
        this.lunchCount = lunchCount;
        this.dinnerCount = dinnerCount;
        this.totalAttendance = totalAttendance;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getBreakfastCount() {
        return breakfastCount;
    }

    public void setBreakfastCount(long breakfastCount) {
        this.breakfastCount = breakfastCount;
    }

    public long getLunchCount() {
        return lunchCount;
    }

    public void setLunchCount(long lunchCount) {
        this.lunchCount = lunchCount;
    }

    public long getDinnerCount() {
        return dinnerCount;
    }

    public void setDinnerCount(long dinnerCount) {
        this.dinnerCount = dinnerCount;
    }

    public long getTotalAttendance() {
        return totalAttendance;
    }

    public void setTotalAttendance(long totalAttendance) {
        this.totalAttendance = totalAttendance;
    }
}
