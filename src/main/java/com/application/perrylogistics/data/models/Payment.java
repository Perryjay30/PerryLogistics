package com.application.perrylogistics.data.models;

import lombok.Data;

@Data
//@AllArgsConstructor
public class Payment {
//    private String id;
    private double weight;
//    private String orderId;
    private final double price = 1000.00;
//    private PackageCategory packageCategory;
//    private double amountToPay = getPrice() * getWeight();


}
