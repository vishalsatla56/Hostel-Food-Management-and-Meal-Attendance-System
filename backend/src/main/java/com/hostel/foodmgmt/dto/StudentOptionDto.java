package com.hostel.foodmgmt.dto;

public class StudentOptionDto {

    private String id;
    private String studentCode;
    private String fullName;
    private String roomNumber;
    private String hostelBlock;
    private String department;

    public StudentOptionDto() {
    }

    public StudentOptionDto(String id, String studentCode, String fullName, String roomNumber, String hostelBlock, String department) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.roomNumber = roomNumber;
        this.hostelBlock = hostelBlock;
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
