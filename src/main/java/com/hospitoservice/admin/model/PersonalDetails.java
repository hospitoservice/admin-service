package com.hospitoservice.admin.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonalDetails {
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String photoUrl;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String maritalStatus;
    private String nationality;
    private String[] languagesKnown;
}
