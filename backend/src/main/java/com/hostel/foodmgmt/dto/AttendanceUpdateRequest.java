package com.hostel.foodmgmt.dto;

import com.hostel.foodmgmt.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public class AttendanceUpdateRequest {

    @NotNull(message = "Status is required")
    private AttendanceStatus status;

    public AttendanceUpdateRequest() {
    }

    public AttendanceUpdateRequest(AttendanceStatus status) {
        this.status = status;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
