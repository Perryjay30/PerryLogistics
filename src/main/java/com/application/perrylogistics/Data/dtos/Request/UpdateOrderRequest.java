package com.application.perrylogistics.Data.dtos.Request;

import com.application.perrylogistics.Data.Models.PackageCategory;
import lombok.Data;

@Data
public class UpdateOrderRequest {
    private String id;
    private String packageName;
    private PackageCategory packageCategory;
    private String pickUpAddress;
    private String destination;
    private String receiverName;
    private String receiverPhoneNumber;
    private String receiverEmail;
    private double weight;
}
