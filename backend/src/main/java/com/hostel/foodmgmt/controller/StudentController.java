package com.hostel.foodmgmt.controller;

import com.hostel.foodmgmt.dto.AttendanceRecordDto;
import com.hostel.foodmgmt.dto.StudentDashboardResponse;
import com.hostel.foodmgmt.model.MealType;
import com.hostel.foodmgmt.service.StudentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<StudentDashboardResponse> getDashboard(Authentication authentication) {
        String username = authentication.getName();
        StudentDashboardResponse response = studentService.getDashboardSummary(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<AttendanceRecordDto>> getAttendanceHistory(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) MealType mealType) {
        String username = authentication.getName();
        List<AttendanceRecordDto> history = studentService.getAttendanceHistory(username, startDate, endDate, mealType);
        return ResponseEntity.ok(history);
    }
}
