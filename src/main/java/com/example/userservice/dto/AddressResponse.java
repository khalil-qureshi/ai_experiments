package com.example.userservice.dto;

import com.example.userservice.entity.Address;

import java.util.UUID;

public class AddressResponse {
    private UUID id;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    public static AddressResponse fromEntity(Address address) {
        AddressResponse response = new AddressResponse();
        response.id = address.getId();
        response.line1 = address.getLine1();
        response.line2 = address.getLine2();
        response.city = address.getCity();
        response.state = address.getState();
        response.postalCode = address.getPostalCode();
        response.country = address.getCountry();
        return response;
    }

    public UUID getId() {
        return id;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }
}
