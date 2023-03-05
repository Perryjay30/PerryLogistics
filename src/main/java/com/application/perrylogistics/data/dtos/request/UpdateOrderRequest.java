package com.application.perrylogistics.data.dtos.request;

import com.application.perrylogistics.data.models.PackageCategory;
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
