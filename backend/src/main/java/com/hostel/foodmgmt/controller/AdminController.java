package com.hostel.foodmgmt.controller;

import com.hostel.foodmgmt.dto.AdminDashboardResponse;
import com.hostel.foodmgmt.dto.AttendanceEntryRequest;
import com.hostel.foodmgmt.dto.AttendanceListResponse;
import com.hostel.foodmgmt.dto.AttendanceRecordDto;
import com.hostel.foodmgmt.dto.AttendanceUpdateRequest;
import com.hostel.foodmgmt.dto.MealPreparationRequest;
import com.hostel.foodmgmt.dto.StudentOptionDto;
import com.hostel.foodmgmt.dto.SufficiencyCalculationResponse;
import com.hostel.foodmgmt.model.MealType;
import com.hostel.foodmgmt.service.AdminDashboardService;
import com.hostel.foodmgmt.service.AttendanceService;
import com.hostel.foodmgmt.service.MealPreparationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminDashboardService adminDashboardService;
    private final AttendanceService attendanceService;
    private final MealPreparationService mealPreparationService;

    public AdminController(AdminDashboardService adminDashboardService,
                           AttendanceService attendanceService,
                           MealPreparationService mealPreparationService) {
        this.adminDashboardService = adminDashboardService;
        this.attendanceService = attendanceService;
        this.mealPreparationService = mealPreparationService;
    }

    // 1. Admin Dashboard Stats
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {
        AdminDashboardResponse response = adminDashboardService.getDashboardStats(selectedDate);
        return ResponseEntity.ok(response);
    }

    // 2. Student list for attendance selection
    @GetMapping("/students")
    public ResponseEntity<List<StudentOptionDto>> getStudents() {
        List<StudentOptionDto> students = attendanceService.getAllActiveStudents();
        return ResponseEntity.ok(students);
    }

    // 3. Attendance Entry: View list for date & meal
    @GetMapping("/attendance")
    public ResponseEntity<AttendanceListResponse> getAttendanceList(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) MealType mealType) {
        AttendanceListResponse response = attendanceService.getAttendanceList(date, mealType);
        return ResponseEntity.ok(response);
    }

    // 4. Attendance Entry: Record new attendance
    @PostMapping("/attendance")
    public ResponseEntity<AttendanceRecordDto> recordAttendance(
            @Valid @RequestBody AttendanceEntryRequest request,
            Authentication authentication) {
        String adminUsername = authentication != null ? authentication.getName() : "admin";
        AttendanceRecordDto record = attendanceService.recordAttendance(request, adminUsername);
        return new ResponseEntity<>(record, HttpStatus.CREATED);
    }

    // 5. Attendance Entry: Edit existing attendance record
    @PutMapping("/attendance/{id}")
    public ResponseEntity<AttendanceRecordDto> updateAttendance(
            @PathVariable String id,
            @Valid @RequestBody AttendanceUpdateRequest request,
            Authentication authentication) {
        String adminUsername = authentication != null ? authentication.getName() : "admin";
        AttendanceRecordDto updated = attendanceService.updateAttendance(id, request, adminUsername);
        return ResponseEntity.ok(updated);
    }

    // 6. Delete attendance record if needed
    @DeleteMapping("/attendance/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable String id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }

    // 7. Food Sufficiency: Calculate & retrieve sufficiency results
    @GetMapping("/sufficiency")
    public ResponseEntity<SufficiencyCalculationResponse> getSufficiency(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) MealType mealType) {
        SufficiencyCalculationResponse response = mealPreparationService.calculateSufficiency(date, mealType);
        return ResponseEntity.ok(response);
    }

    // 8. Food Sufficiency: Enter or update meals prepared
    @PostMapping("/sufficiency")
    public ResponseEntity<SufficiencyCalculationResponse> savePreparation(
            @Valid @RequestBody MealPreparationRequest request) {
        SufficiencyCalculationResponse response = mealPreparationService.saveOrUpdatePreparation(request);
        return ResponseEntity.ok(response);
    }
}
