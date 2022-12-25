package com.application.perrylogistics.Data.dtos.Request;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String firstName;
    private String LastName;
    private String email;
    private String password;
    private String PhoneNumber;
    private String Address;
}
