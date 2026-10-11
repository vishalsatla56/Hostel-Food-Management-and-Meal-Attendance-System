package com.hostel.foodmgmt.service;

import com.hostel.foodmgmt.dto.AttendanceEntryRequest;
import com.hostel.foodmgmt.dto.AttendanceListResponse;
import com.hostel.foodmgmt.dto.AttendanceRecordDto;
import com.hostel.foodmgmt.dto.AttendanceUpdateRequest;
import com.hostel.foodmgmt.dto.StudentOptionDto;
import com.hostel.foodmgmt.exception.DuplicateRecordException;
import com.hostel.foodmgmt.exception.ResourceNotFoundException;
import com.hostel.foodmgmt.model.Attendance;
import com.hostel.foodmgmt.model.AttendanceStatus;
import com.hostel.foodmgmt.model.MealType;
import com.hostel.foodmgmt.model.Student;
import com.hostel.foodmgmt.repository.AttendanceRepository;
import com.hostel.foodmgmt.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    public AttendanceRecordDto recordAttendance(AttendanceEntryRequest request, String adminUsername) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));

        Optional<Attendance> existing = attendanceRepository.findByStudentIdAndDateAndMealType(
                request.getStudentId(), request.getDate(), request.getMealType());

        if (existing.isPresent()) {
            throw new DuplicateRecordException("Duplicate entry: Attendance record already exists for "
                    + student.getFullName() + " (" + student.getStudentCode() + ") on "
                    + request.getDate() + " for " + request.getMealType() + ".");
        }

        Attendance attendance = new Attendance(
                student.getId(),
                student.getStudentCode(),
                student.getFullName(),
                request.getDate(),
                request.getMealType(),
                request.getStatus(),
                adminUsername
        );

        Attendance saved = attendanceRepository.save(attendance);
        return toDto(saved);
    }

    public AttendanceRecordDto updateAttendance(String attendanceId, AttendanceUpdateRequest request, String adminUsername) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with ID: " + attendanceId));

        attendance.setStatus(request.getStatus());
        attendance.setRecordedBy(adminUsername);
        attendance.setRecordedAt(LocalDateTime.now());

        Attendance saved = attendanceRepository.save(attendance);
        return toDto(saved);
    }

    public void deleteAttendance(String attendanceId) {
        if (!attendanceRepository.existsById(attendanceId)) {
            throw new ResourceNotFoundException("Attendance record not found with ID: " + attendanceId);
        }
        attendanceRepository.deleteById(attendanceId);
    }

    public AttendanceListResponse getAttendanceList(LocalDate date, MealType mealType) {
        if (date == null) {
            date = LocalDate.now();
        }
        if (mealType == null) {
            mealType = MealType.LUNCH;
        }

        List<Attendance> records = attendanceRepository.findByDateAndMealType(date, mealType);
        records.sort(Comparator.comparing(Attendance::getRecordedAt, Comparator.nullsLast(Comparator.reverseOrder())));

        long presentCount = records.stream().filter(r -> r.getStatus() == AttendanceStatus.PRESENT).count();
        long absentCount = records.stream().filter(r -> r.getStatus() == AttendanceStatus.ABSENT).count();

        List<AttendanceRecordDto> dtos = records.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new AttendanceListResponse(date, mealType, presentCount, absentCount, dtos);
    }

    public List<StudentOptionDto> getAllActiveStudents() {
        return studentRepository.findByActiveTrue().stream()
                .sorted(Comparator.comparing(Student::getStudentCode))
                .map(s -> new StudentOptionDto(
                        s.getId(),
                        s.getStudentCode(),
                        s.getFullName(),
                        s.getRoomNumber(),
                        s.getHostelBlock(),
                        s.getDepartment()
                ))
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
