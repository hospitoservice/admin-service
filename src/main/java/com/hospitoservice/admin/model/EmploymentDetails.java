package com.hospitoservice.admin.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmploymentDetails {
    private String employeeId;
    private String role;
    private String department;
    private String designation;
    private String specialization;
    private String subSpecialization;
    private String workLocation;
    private String reportingManagerId;
    private String employmentType;
    private LocalDate joiningDate;
    private LocalDate probationEndDate;
    private LocalDate confirmationDate;
    private String employmentStatus;
    private LocalDate exitDate;
    private String exitReason;
    private String[] allowedShiftTypes;
    private Integer maxWeeklyHours;
    private Boolean onCallAvailability;
}
