package com.hostel.foodmgmt.dto;

import java.time.LocalDate;

public class AdminDashboardResponse {

    private long totalRegisteredStudents;
    private long totalActiveStudents;

    private long todayBreakfastCount;
    private long todayLunchCount;
    private long todayDinnerCount;
    private long totalMealAttendance;

    private LocalDate selectedDate;
    private DateStatsDto selectedDateStats;

    public AdminDashboardResponse() {
    }

    public long getTotalRegisteredStudents() {
        return totalRegisteredStudents;
    }

    public void setTotalRegisteredStudents(long totalRegisteredStudents) {
        this.totalRegisteredStudents = totalRegisteredStudents;
    }

    public long getTotalActiveStudents() {
        return totalActiveStudents;
    }

    public void setTotalActiveStudents(long totalActiveStudents) {
        this.totalActiveStudents = totalActiveStudents;
    }

    public long getTodayBreakfastCount() {
        return todayBreakfastCount;
    }

    public void setTodayBreakfastCount(long todayBreakfastCount) {
        this.todayBreakfastCount = todayBreakfastCount;
    }

    public long getTodayLunchCount() {
        return todayLunchCount;
    }

    public void setTodayLunchCount(long todayLunchCount) {
        this.todayLunchCount = todayLunchCount;
    }

    public long getTodayDinnerCount() {
        return todayDinnerCount;
    }

    public void setTodayDinnerCount(long todayDinnerCount) {
        this.todayDinnerCount = todayDinnerCount;
    }

    public long getTotalMealAttendance() {
        return totalMealAttendance;
    }

    public void setTotalMealAttendance(long totalMealAttendance) {
        this.totalMealAttendance = totalMealAttendance;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public DateStatsDto getSelectedDateStats() {
        return selectedDateStats;
    }

    public void setSelectedDateStats(DateStatsDto selectedDateStats) {
        this.selectedDateStats = selectedDateStats;
    }
}
