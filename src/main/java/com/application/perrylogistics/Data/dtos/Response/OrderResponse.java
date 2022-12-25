package com.application.perrylogistics.Data.dtos.Response;

import lombok.Data;

@Data
public class OrderResponse {
    private String id;
    private int statusCode;
    private String message;
}
