package com.application.perrylogistics.Data.dtos.Request;

import com.application.perrylogistics.Data.Models.PackageCategory;
import com.application.perrylogistics.Data.Models.Payment;
import com.application.perrylogistics.utils.Validators.UserDetailsValidators;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    private String customerId;
    private String packageName;
    private PackageCategory packageCategory;
    private String pickUpAddress;
    private String destination;
    private String receiverName;
    private String receiverPhoneNumber;
    private String receiverEmail;
    private double weight;

    public String getReceiverPhoneNumber() {
        if(!UserDetailsValidators.isValidPhoneNumber(receiverPhoneNumber)) {
            throw new RuntimeException("Phone number must be a length of 11");
        }
        return receiverPhoneNumber;
    }


}
