package com.hospitoservice.admin.model;

import lombok.Data;

@Data
public class PayrollDetails {
    private String bankAccountNumber;
    private String bankName;
    private String ifscCode;
    private String paymentMode;
    private Double baseSalary;
    private Double allowances;
    private Double deductions;
    private String pfNumber;
    private String esiNumber;
}
