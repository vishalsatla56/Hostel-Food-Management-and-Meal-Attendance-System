package com.hostel.foodmgmt.service;

import com.hostel.foodmgmt.config.JwtUtils;
import com.hostel.foodmgmt.dto.AuthResponse;
import com.hostel.foodmgmt.dto.LoginRequest;
import com.hostel.foodmgmt.exception.ResourceNotFoundException;
import com.hostel.foodmgmt.model.Role;
import com.hostel.foodmgmt.model.Student;
import com.hostel.foodmgmt.model.User;
import com.hostel.foodmgmt.repository.StudentRepository;
import com.hostel.foodmgmt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       StudentRepository studentRepository,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + loginRequest.getUsername()));

        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN || user.getRole() == Role.ADMIN;
        Role normalizedRole = isAdmin ? Role.ROLE_ADMIN : Role.ROLE_STUDENT;
        String token = jwtUtils.generateToken(user.getUsername(), normalizedRole.name());

        String studentId = user.getStudentId();
        String studentCode = null;
        String fullName = user.getFullName();

        if (!isAdmin && studentId != null) {
            Student student = studentRepository.findById(studentId).orElse(null);
            if (student != null) {
                studentCode = student.getStudentCode();
                if (fullName == null || fullName.isBlank()) {
                    fullName = student.getFullName();
                }
            }
        }

        return new AuthResponse(
                token,
                user.getUsername(),
                normalizedRole,
                studentId,
                studentCode,
                fullName
        );
    }
}
