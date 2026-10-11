package com.hostel.foodmgmt.dto;

import com.hostel.foodmgmt.model.Role;

public class AuthResponse {

    private String token;
    private String username;
    private Role role;
    private String studentId;
    private String studentCode;
    private String fullName;

    public AuthResponse() {
    }

    public AuthResponse(String token, String username, Role role, String studentId, String studentCode, String fullName) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.studentId = studentId;
        this.studentCode = studentCode;
        this.fullName = fullName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
}
