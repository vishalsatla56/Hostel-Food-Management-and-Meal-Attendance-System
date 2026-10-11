package com.hostel.foodmgmt.service;

import com.hostel.foodmgmt.dto.AdminDashboardResponse;
import com.hostel.foodmgmt.dto.DateStatsDto;
import com.hostel.foodmgmt.model.AttendanceStatus;
import com.hostel.foodmgmt.model.MealType;
import com.hostel.foodmgmt.repository.AttendanceRepository;
import com.hostel.foodmgmt.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AdminDashboardService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public AdminDashboardService(StudentRepository studentRepository,
                                 AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public AdminDashboardResponse getDashboardStats(LocalDate selectedDate) {
        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }

        LocalDate today = LocalDate.now();

        long totalRegistered = studentRepository.count();
        long totalActive = studentRepository.countByActiveTrue();

        long todayBreakfast = attendanceRepository.countByDateAndMealTypeAndStatus(
                today, MealType.BREAKFAST, AttendanceStatus.PRESENT);
        long todayLunch = attendanceRepository.countByDateAndMealTypeAndStatus(
                today, MealType.LUNCH, AttendanceStatus.PRESENT);
        long todayDinner = attendanceRepository.countByDateAndMealTypeAndStatus(
                today, MealType.DINNER, AttendanceStatus.PRESENT);

        long totalMealAttendance = attendanceRepository.countByStatus(AttendanceStatus.PRESENT);

        // Selected date stats
        long selBreakfast = attendanceRepository.countByDateAndMealTypeAndStatus(
                selectedDate, MealType.BREAKFAST, AttendanceStatus.PRESENT);
        long selLunch = attendanceRepository.countByDateAndMealTypeAndStatus(
                selectedDate, MealType.LUNCH, AttendanceStatus.PRESENT);
        long selDinner = attendanceRepository.countByDateAndMealTypeAndStatus(
                selectedDate, MealType.DINNER, AttendanceStatus.PRESENT);
        long selTotal = selBreakfast + selLunch + selDinner;

        DateStatsDto dateStats = new DateStatsDto(selectedDate, selBreakfast, selLunch, selDinner, selTotal);

        AdminDashboardResponse response = new AdminDashboardResponse();
        response.setTotalRegisteredStudents(totalRegistered);
        response.setTotalActiveStudents(totalActive);
        response.setTodayBreakfastCount(todayBreakfast);
        response.setTodayLunchCount(todayLunch);
        response.setTodayDinnerCount(todayDinner);
        response.setTotalMealAttendance(totalMealAttendance);
        response.setSelectedDate(selectedDate);
        response.setSelectedDateStats(dateStats);

        return response;
    }
}
