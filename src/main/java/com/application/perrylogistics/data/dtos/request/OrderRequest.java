package com.application.perrylogistics.data.dtos.request;

import com.application.perrylogistics.data.models.PackageCategory;
import com.application.perrylogistics.utils.validators.UserDetailsValidators;
import lombok.Data;

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
