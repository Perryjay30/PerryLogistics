package com.application.perrylogistics.data.dtos.response;

import lombok.Data;

@Data
public class OrderResponse {
    private String id;
    private int statusCode;
    private String message;
}
