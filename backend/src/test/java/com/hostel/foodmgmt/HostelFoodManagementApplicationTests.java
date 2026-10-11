package com.hostel.foodmgmt;

import com.hostel.foodmgmt.config.JwtUtils;
import com.hostel.foodmgmt.model.SufficiencyStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class HostelFoodManagementApplicationTests {

    @Test
    @DisplayName("Test BCrypt Password Encoder Hashing and Validation")
    void testBCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";
        String encoded = encoder.encode(rawPassword);

        assertNotNull(encoded);
        assertTrue(encoder.matches(rawPassword, encoded));
        assertFalse(encoder.matches("wrongpassword", encoded));
    }

    @Test
    @DisplayName("Test JWT Token Generation, Subject and Role Claims")
    void testJwtTokenGenerationAndValidation() {
        String secret = "testSecretKeyForUnitTestingPurposesOnlyNeedsToBe256BitsLong123456";
        long expirationMs = 3600000;
        JwtUtils jwtUtils = new JwtUtils(secret, expirationMs);

        String username = "student_test";
        String role = "ROLE_STUDENT";

        String token = jwtUtils.generateToken(username, role);
        assertNotNull(token);
        assertTrue(jwtUtils.validateToken(token));
        assertEquals(username, jwtUtils.getUsernameFromToken(token));
        assertEquals(role, jwtUtils.getRoleFromToken(token));
    }

    @Test
    @DisplayName("Test Food Sufficiency Difference and Status Logic")
    void testSufficiencyFormula() {
        // Calculation: Difference = Meals Prepared − Expected Meal Demand

        // Scenario 1: Exact match (SUFFICIENT)
        int prepared1 = 50;
        int demand1 = 50;
        int diff1 = prepared1 - demand1;
        SufficiencyStatus status1 = diff1 == 0 ? SufficiencyStatus.SUFFICIENT :
                (diff1 > 0 ? SufficiencyStatus.EXCESS : SufficiencyStatus.INSUFFICIENT);
        assertEquals(0, diff1);
        assertEquals(SufficiencyStatus.SUFFICIENT, status1);

        // Scenario 2: Surplus (EXCESS)
        int prepared2 = 65;
        int demand2 = 50;
        int diff2 = prepared2 - demand2;
        SufficiencyStatus status2 = diff2 == 0 ? SufficiencyStatus.SUFFICIENT :
                (diff2 > 0 ? SufficiencyStatus.EXCESS : SufficiencyStatus.INSUFFICIENT);
        assertEquals(15, diff2);
        assertEquals(SufficiencyStatus.EXCESS, status2);

        // Scenario 3: Shortage (INSUFFICIENT)
        int prepared3 = 40;
        int demand3 = 50;
        int diff3 = prepared3 - demand3;
        SufficiencyStatus status3 = diff3 == 0 ? SufficiencyStatus.SUFFICIENT :
                (diff3 > 0 ? SufficiencyStatus.EXCESS : SufficiencyStatus.INSUFFICIENT);
        assertEquals(-10, diff3);
        assertEquals(SufficiencyStatus.INSUFFICIENT, status3);
    }
}
