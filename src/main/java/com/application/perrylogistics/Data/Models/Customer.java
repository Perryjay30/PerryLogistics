package com.application.perrylogistics.Data.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Data
@Document
public class Customer {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String password;
    private String phoneNumber;
    private Status status;
    @DBRef
    private List<Order> customerOrders = new ArrayList<>();
}
