package com.hostel.foodmgmt.service;

import com.hostel.foodmgmt.dto.AttendanceRecordDto;
import com.hostel.foodmgmt.dto.MealStatusItem;
import com.hostel.foodmgmt.dto.StudentDashboardResponse;
import com.hostel.foodmgmt.exception.ResourceNotFoundException;
import com.hostel.foodmgmt.model.Attendance;
import com.hostel.foodmgmt.model.AttendanceStatus;
import com.hostel.foodmgmt.model.MealType;
import com.hostel.foodmgmt.model.Student;
import com.hostel.foodmgmt.model.User;
import com.hostel.foodmgmt.repository.AttendanceRepository;
import com.hostel.foodmgmt.repository.StudentRepository;
import com.hostel.foodmgmt.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    public StudentService(UserRepository userRepository,
                          StudentRepository studentRepository,
                          AttendanceRepository attendanceRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public Student getStudentByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (user.getStudentId() == null) {
            throw new ResourceNotFoundException("No student profile linked to user: " + username);
        }

        return studentRepository.findById(user.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for ID: " + user.getStudentId()));
    }

    public StudentDashboardResponse getDashboardSummary(String username) {
        Student student = getStudentByUsername(username);
        String studentId = student.getId();
        LocalDate today = LocalDate.now();

        StudentDashboardResponse response = new StudentDashboardResponse();
        response.setStudentId(student.getId());
        response.setStudentCode(student.getStudentCode());
        response.setFullName(student.getFullName());
        response.setRoomNumber(student.getRoomNumber());
        response.setHostelBlock(student.getHostelBlock());
        response.setDepartment(student.getDepartment());

        // Total attended
        long totalAttended = attendanceRepository.countByStudentIdAndStatus(studentId, AttendanceStatus.PRESENT);
        response.setTotalMealsAttended(totalAttended);

        // Meal breakdown
        response.setBreakfastCount(attendanceRepository.countByStudentIdAndMealTypeAndStatus(studentId, MealType.BREAKFAST, AttendanceStatus.PRESENT));
        response.setLunchCount(attendanceRepository.countByStudentIdAndMealTypeAndStatus(studentId, MealType.LUNCH, AttendanceStatus.PRESENT));
        response.setDinnerCount(attendanceRepository.countByStudentIdAndMealTypeAndStatus(studentId, MealType.DINNER, AttendanceStatus.PRESENT));

        // Today's attendance
        List<Attendance> todayRecords = attendanceRepository.findByStudentIdAndDate(studentId, today);
        Map<MealType, Attendance> todayMap = todayRecords.stream()
                .collect(Collectors.toMap(Attendance::getMealType, a -> a, (a1, a2) -> a1));

        List<MealStatusItem> todayStatuses = new ArrayList<>();
        for (MealType meal : MealType.values()) {
            Attendance rec = todayMap.get(meal);
            if (rec != null) {
                String formattedTime = rec.getRecordedAt() != null ? rec.getRecordedAt().format(TIME_FORMATTER) : "";
                todayStatuses.add(new MealStatusItem(meal, rec.getStatus().name(), formattedTime));
            } else {
                todayStatuses.add(new MealStatusItem(meal, "NOT_RECORDED", "-"));
            }
        }
        response.setTodayStatus(todayStatuses);

        // Current month's attendance
        LocalDate startOfMonth = today.withDayOfMonth(1);
        long currentMonthCount = attendanceRepository.countByStudentIdAndDateBetweenAndStatus(
                studentId, startOfMonth, today, AttendanceStatus.PRESENT);
        response.setCurrentMonthAttendanceCount(currentMonthCount);

        // Recent meal attendance records (up to 10)
        List<Attendance> allStudentRecords = attendanceRepository.findByStudentIdOrderByDateDescRecordedAtDesc(studentId);
        List<AttendanceRecordDto> recent = allStudentRecords.stream()
                .limit(10)
                .map(this::toDto)
                .collect(Collectors.toList());
        response.setRecentRecords(recent);

        return response;
    }

    public List<AttendanceRecordDto> getAttendanceHistory(String username, LocalDate startDate, LocalDate endDate, MealType mealType) {
        Student student = getStudentByUsername(username);
        String studentId = student.getId();

        List<Attendance> records;

        if (startDate != null && endDate != null && mealType != null) {
            records = attendanceRepository.findByStudentIdAndDateBetweenAndMealTypeOrderByDateDescRecordedAtDesc(
                    studentId, startDate, endDate, mealType);
        } else if (startDate != null && endDate != null) {
            records = attendanceRepository.findByStudentIdAndDateBetweenOrderByDateDescRecordedAtDesc(
                    studentId, startDate, endDate);
        } else if (mealType != null) {
            records = attendanceRepository.findByStudentIdAndMealTypeOrderByDateDescRecordedAtDesc(
                    studentId, mealType);
        } else {
            records = attendanceRepository.findByStudentIdOrderByDateDescRecordedAtDesc(studentId);
        }

        return records.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public AttendanceRecordDto toDto(Attendance attendance) {
        return new AttendanceRecordDto(
                attendance.getId(),
                attendance.getStudentId(),
                attendance.getStudentCode(),
                attendance.getStudentName(),
                attendance.getDate(),
                attendance.getMealType(),
                attendance.getStatus(),
                attendance.getRecordedAt(),
                attendance.getRecordedBy()
        );
    }
}
