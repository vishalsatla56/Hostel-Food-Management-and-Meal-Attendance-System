package com.hostel.foodmgmt.service;

import com.hostel.foodmgmt.dto.MealPreparationRequest;
import com.hostel.foodmgmt.dto.SufficiencyCalculationResponse;
import com.hostel.foodmgmt.model.Attendance;
import com.hostel.foodmgmt.model.AttendanceStatus;
import com.hostel.foodmgmt.model.MealPreparation;
import com.hostel.foodmgmt.model.MealType;
import com.hostel.foodmgmt.model.SufficiencyStatus;
import com.hostel.foodmgmt.repository.AttendanceRepository;
import com.hostel.foodmgmt.repository.MealPreparationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MealPreparationService {

    private final MealPreparationRepository mealPreparationRepository;
    private final AttendanceRepository attendanceRepository;

    public MealPreparationService(MealPreparationRepository mealPreparationRepository,
                                  AttendanceRepository attendanceRepository) {
        this.mealPreparationRepository = mealPreparationRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public SufficiencyCalculationResponse saveOrUpdatePreparation(MealPreparationRequest request) {
        Optional<MealPreparation> existing = mealPreparationRepository.findByDateAndMealType(
                request.getDate(), request.getMealType());

        MealPreparation prep;
        if (existing.isPresent()) {
            prep = existing.get();
            prep.setMealsPrepared(request.getMealsPrepared());
            prep.setNotes(request.getNotes());
            prep.setUpdatedAt(LocalDateTime.now());
        } else {
            prep = new MealPreparation(
                    request.getDate(),
                    request.getMealType(),
                    request.getMealsPrepared(),
                    request.getNotes()
            );
        }

        mealPreparationRepository.save(prep);
        return calculateSufficiency(request.getDate(), request.getMealType());
    }

    public SufficiencyCalculationResponse calculateSufficiency(LocalDate date, MealType mealType) {
        if (date == null) {
            date = LocalDate.now();
        }
        if (mealType == null) {
            mealType = MealType.LUNCH;
        }

        // 1. Retrieve actual recorded attendance (PRESENT count) from MongoDB
        int actualAttendance = (int) attendanceRepository.countByDateAndMealTypeAndStatus(
                date, mealType, AttendanceStatus.PRESENT);

        // 2. Retrieve meal preparation record from MongoDB
        Optional<MealPreparation> prepOpt = mealPreparationRepository.findByDateAndMealType(date, mealType);
        int mealsPrepared = prepOpt.map(MealPreparation::getMealsPrepared).orElse(0);
        String notes = prepOpt.map(MealPreparation::getNotes).orElse("");
        boolean prepExists = prepOpt.isPresent();

        // 3. Baseline demand calculation: Expected meal demand = recorded attendance
        int expectedDemand = actualAttendance;

        // 4. Difference = Meals Prepared − Expected Meal Demand
        int difference = mealsPrepared - expectedDemand;

        SufficiencyStatus status;
        int statusQuantity = Math.abs(difference);
        String statusMessage;

        if (difference == 0) {
            status = SufficiencyStatus.SUFFICIENT;
            statusMessage = "SUFFICIENT (Exact match between meals prepared and actual demand)";
        } else if (difference > 0) {
            status = SufficiencyStatus.EXCESS;
            statusMessage = "EXCESS (Surplus of " + statusQuantity + " meal" + (statusQuantity > 1 ? "s" : "") + ")";
        } else {
            status = SufficiencyStatus.INSUFFICIENT;
            statusMessage = "INSUFFICIENT (Shortage of " + statusQuantity + " meal" + (statusQuantity > 1 ? "s" : "") + ")";
        }

        SufficiencyCalculationResponse response = new SufficiencyCalculationResponse();
        response.setDate(date);
        response.setMealType(mealType);
        response.setMealsPrepared(mealsPrepared);
        response.setActualAttendance(actualAttendance);
        response.setExpectedDemand(expectedDemand);
        response.setDifference(difference);
        response.setStatus(status);
        response.setStatusQuantity(statusQuantity);
        response.setStatusMessage(statusMessage);
        response.setCalculationMethod("Baseline (Recorded Attendance)");
        response.setBaselineNote("Baseline calculation: Expected meal demand equals recorded attendance. This is a deterministic baseline calculation, not machine-learning prediction.");
        response.setPreparationRecordExists(prepExists);
        response.setNotes(notes);

        // 5. Historical attendance data analysis (Optional prediction method)
        calculateHistoricalAverage(response, date, mealType, mealsPrepared);

        return response;
    }

    private void calculateHistoricalAverage(SufficiencyCalculationResponse response,
                                           LocalDate targetDate,
                                           MealType mealType,
                                           int mealsPrepared) {
        // Find past PRESENT attendance records for the same meal type prior to the selected date
        List<Attendance> pastAttendances = attendanceRepository.findByMealTypeAndDateBeforeAndStatus(
                mealType, targetDate, AttendanceStatus.PRESENT);

        // Group attendances by past date to get daily attendance counts
        Map<LocalDate, Long> dailyCounts = pastAttendances.stream()
                .collect(Collectors.groupingBy(Attendance::getDate, Collectors.counting()));

        int historicalDaysCount = dailyCounts.size();
        response.setHistoricalRecordCount(historicalDaysCount);
        response.setHistoricalMethod("Historical Daily Average (" + historicalDaysCount + " past recorded dates)");

        // Require at least 3 distinct past recorded dates to provide a historical-average prediction
        if (historicalDaysCount >= 3) {
            double sum = dailyCounts.values().stream().mapToDouble(Long::doubleValue).sum();
            double average = Math.round((sum / historicalDaysCount) * 10.0) / 10.0;
            int roundedDemand = (int) Math.round(average);
            int histDiff = mealsPrepared - roundedDemand;

            SufficiencyStatus histStatus;
            String histMessage;
            if (histDiff == 0) {
                histStatus = SufficiencyStatus.SUFFICIENT;
                histMessage = "SUFFICIENT (Meals prepared match historical average demand)";
            } else if (histDiff > 0) {
                histStatus = SufficiencyStatus.EXCESS;
                histMessage = "EXCESS (Surplus of " + histDiff + " meals relative to historical average)";
            } else {
                histStatus = SufficiencyStatus.INSUFFICIENT;
                histMessage = "INSUFFICIENT (Shortage of " + Math.abs(histDiff) + " meals relative to historical average)";
            }

            response.setHasHistoricalData(true);
            response.setHistoricalAverageDemand(average);
            response.setHistoricalDifference(histDiff);
            response.setHistoricalStatus(histStatus);
            response.setHistoricalStatusMessage(histMessage);
        } else {
            // "Do not invent predictions when historical data is insufficient."
            response.setHasHistoricalData(false);
            response.setHistoricalAverageDemand(null);
            response.setHistoricalDifference(null);
            response.setHistoricalStatus(null);
            response.setHistoricalStatusMessage("Insufficient historical data (< 3 prior dates recorded). Historical demand estimation requires at least 3 prior recorded dates.");
        }
    }
}
