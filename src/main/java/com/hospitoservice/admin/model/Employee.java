package com.hospitoservice.admin.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;
    
    private PersonalDetails personalDetails;
    private EmploymentDetails employmentDetails;
    private ContactDetails contactDetails;
    private List<Address> addresses;
    private DocumentDetails documentDetails;
    private PayrollDetails payrollDetails;
    
    // Authentication & Authorization
    private Boolean hasPortalAccess;
    private String username;
    private String password;
    private List<String> roles;
    private List<String> permissions;
    private LocalDateTime lastLogin;
    
    // Audit Fields
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String remarks;
}
