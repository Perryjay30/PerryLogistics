package com.application.perrylogistics.Data.dtos.Request;

import lombok.Data;

@Data
public class UpdateRequest {
    private String id;
    private String phone;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
}
