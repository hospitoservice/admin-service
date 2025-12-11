package com.hospitoservice.admin.model;

import lombok.Data;

@Data
public class Address {
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private AddressType type;
    
    public enum AddressType {
        PRESENT,
        PERMANENT
    }
}
