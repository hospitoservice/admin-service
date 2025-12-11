package com.hospitoservice.admin.model;

import lombok.Data;

@Data
public class ContactDetails {
    private String email;
    private String phone;
    private String secondaryPhone;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactPhone;
}
