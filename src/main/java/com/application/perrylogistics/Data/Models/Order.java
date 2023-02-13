package com.application.perrylogistics.Data.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Order {
    @Id
    private String id;
    private String packageName;
    private PackageCategory packageCategory;
    private String pickUpAddress;
    private String destination;
    private String receiverName;
    private String receiverPhoneNumber;
    private String receiverEmail;
    private double price = 1000.00;
    private double weight;
    private double amountToPay;
}

