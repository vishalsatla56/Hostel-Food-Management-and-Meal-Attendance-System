package com.hostel.foodmgmt.dto;

import java.util.List;

public class StudentDashboardResponse {

    private String studentId;
    private String studentCode;
    private String fullName;
    private String roomNumber;
    private String hostelBlock;
    private String department;

    private long totalMealsAttended;
    private long breakfastCount;
    private long lunchCount;
    private long dinnerCount;

    private List<MealStatusItem> todayStatus;
    private long currentMonthAttendanceCount;

    private List<AttendanceRecordDto> recentRecords;

    public StudentDashboardResponse() {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getHostelBlock() {
        return hostelBlock;
    }

    public void setHostelBlock(String hostelBlock) {
        this.hostelBlock = hostelBlock;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public long getTotalMealsAttended() {
        return totalMealsAttended;
    }

    public void setTotalMealsAttended(long totalMealsAttended) {
        this.totalMealsAttended = totalMealsAttended;
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

    public List<MealStatusItem> getTodayStatus() {
        return todayStatus;
    }

    public void setTodayStatus(List<MealStatusItem> todayStatus) {
        this.todayStatus = todayStatus;
    }

    public long getCurrentMonthAttendanceCount() {
        return currentMonthAttendanceCount;
    }

    public void setCurrentMonthAttendanceCount(long currentMonthAttendanceCount) {
        this.currentMonthAttendanceCount = currentMonthAttendanceCount;
    }

    public List<AttendanceRecordDto> getRecentRecords() {
        return recentRecords;
    }

    public void setRecentRecords(List<AttendanceRecordDto> recentRecords) {
        this.recentRecords = recentRecords;
    }
}
