package com.hospitoservice.admin.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DocumentDetails {
    private String aadhaarNumber;
    private String panNumber;
    private String passportNumber;
    private String licenseNumber;
    private LocalDate licenseValidTill;
    private String registrationCouncil;
    private String registrationNumber;
    private String taxIdNumber;
    private String medicalFitnessCertificateUrl;
    private String backgroundVerificationStatus;
    private String[] certifications;
    private Boolean accreditationTrainingCompleted;
    private String offerLetterUrl;
    private String employmentContractUrl;
    private String[] experienceCertificates;
    private String idCardUrl;
}
