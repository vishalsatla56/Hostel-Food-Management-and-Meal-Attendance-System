package com.hostel.foodmgmt.config;

import com.hostel.foodmgmt.model.Attendance;
import com.hostel.foodmgmt.model.AttendanceStatus;
import com.hostel.foodmgmt.model.MealPreparation;
import com.hostel.foodmgmt.model.MealType;
import com.hostel.foodmgmt.model.Role;
import com.hostel.foodmgmt.model.Student;
import com.hostel.foodmgmt.model.User;
import com.hostel.foodmgmt.repository.AttendanceRepository;
import com.hostel.foodmgmt.repository.MealPreparationRepository;
import com.hostel.foodmgmt.repository.StudentRepository;
import com.hostel.foodmgmt.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final MealPreparationRepository mealPreparationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           StudentRepository studentRepository,
                           AttendanceRepository attendanceRepository,
                           MealPreparationRepository mealPreparationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.mealPreparationRepository = mealPreparationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        logger.info("Verifying and initializing demo accounts and seed data...");

        // 1. Ensure Admin Account
        User admin = userRepository.findByUsername("admin").orElse(new User());
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ROLE_ADMIN);
        admin.setFullName("System Administrator");
        userRepository.save(admin);

        // 2. Ensure Student Records
        Student s1 = ensureStudent("STU-101", "John Doe", "A-204", "Block A", "Computer Science");
        Student s2 = ensureStudent("STU-102", "Jane Smith", "B-312", "Block B", "Electrical Engineering");
        Student s3 = ensureStudent("STU-103", "Rahul Kumar", "A-105", "Block A", "Mechanical Engineering");
        Student s4 = ensureStudent("STU-104", "Priya Sharma", "C-408", "Block C", "Information Technology");
        Student s5 = ensureStudent("STU-105", "Alex Turner", "B-201", "Block B", "Civil Engineering");

        // 3. Ensure Student User Accounts
        ensureStudentUser("john.doe", "student123", s1);
        ensureStudentUser("jane.smith", "student123", s2);
        ensureStudentUser("rahul.kumar", "student123", s3);
        ensureStudentUser("priya.sharma", "student123", s4);
        ensureStudentUser("alex.turner", "student123", s5);

        // 4. Seed Historical Attendance if empty
        if (attendanceRepository.count() == 0) {
            logger.info("Seeding historical meal attendance data for evaluation...");
            LocalDate today = LocalDate.now();
            List<Student> allStudents = List.of(s1, s2, s3, s4, s5);

            for (int i = 4; i >= 1; i--) {
                LocalDate pastDate = today.minusDays(i);
                for (MealType meal : MealType.values()) {
                    for (int sIdx = 0; sIdx < allStudents.size(); sIdx++) {
                        Student student = allStudents.get(sIdx);
                        AttendanceStatus status = (sIdx == 4 && i % 2 == 0) ? AttendanceStatus.ABSENT : AttendanceStatus.PRESENT;

                        Attendance att = new Attendance(
                                student.getId(),
                                student.getStudentCode(),
                                student.getFullName(),
                                pastDate,
                                meal,
                                status,
                                "admin"
                        );
                        att.setRecordedAt(LocalDateTime.of(pastDate.getYear(), pastDate.getMonth(), pastDate.getDayOfMonth(),
                                meal == MealType.BREAKFAST ? 8 : (meal == MealType.LUNCH ? 12 : 19), 30));
                        attendanceRepository.save(att);
                    }

                    // Historical meal preparation
                    MealPreparation prep = new MealPreparation(pastDate, meal, 5, "Standard batch");
                    prep.setUpdatedAt(LocalDateTime.of(pastDate.getYear(), pastDate.getMonth(), pastDate.getDayOfMonth(),
                            meal == MealType.BREAKFAST ? 7 : (meal == MealType.LUNCH ? 11 : 18), 30));
                    mealPreparationRepository.save(prep);
                }
            }

            // Today's attendance
            for (int sIdx = 0; sIdx < allStudents.size(); sIdx++) {
                Student student = allStudents.get(sIdx);
                AttendanceStatus status = (sIdx == 3) ? AttendanceStatus.ABSENT : AttendanceStatus.PRESENT;
                Attendance att = new Attendance(student.getId(), student.getStudentCode(), student.getFullName(),
                        today, MealType.BREAKFAST, status, "admin");
                att.setRecordedAt(LocalDateTime.now().minusHours(4));
                attendanceRepository.save(att);
            }
            mealPreparationRepository.save(new MealPreparation(today, MealType.BREAKFAST, 5, "Breakfast morning batch"));

            attendanceRepository.save(new Attendance(s1.getId(), s1.getStudentCode(), s1.getFullName(), today, MealType.LUNCH, AttendanceStatus.PRESENT, "admin"));
            attendanceRepository.save(new Attendance(s2.getId(), s2.getStudentCode(), s2.getFullName(), today, MealType.LUNCH, AttendanceStatus.PRESENT, "admin"));
            attendanceRepository.save(new Attendance(s3.getId(), s3.getStudentCode(), s3.getFullName(), today, MealType.LUNCH, AttendanceStatus.PRESENT, "admin"));
            mealPreparationRepository.save(new MealPreparation(today, MealType.LUNCH, 4, "Lunch afternoon batch"));
        }

        logger.info("Demo credentials and seed data ready: admin/admin123 and john.doe/student123");
    }

    private Student ensureStudent(String code, String name, String room, String block, String dept) {
        return studentRepository.findByStudentCode(code).orElseGet(() ->
                studentRepository.save(new Student(code, name, room, block, dept, true))
        );
    }

    private void ensureStudentUser(String username, String rawPassword, Student student) {
        User user = userRepository.findByUsername(username).orElse(new User());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.ROLE_STUDENT);
        user.setStudentId(student.getId());
        user.setFullName(student.getFullName());
        userRepository.save(user);
    }
}
