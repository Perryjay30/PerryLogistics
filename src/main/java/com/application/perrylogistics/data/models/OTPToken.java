package com.application.perrylogistics.data.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document
@RequiredArgsConstructor
public class OTPToken {
    @Id
    private String id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime verifiedAt;
    @DBRef
    private Customer customer;
    @DBRef
    private Courier courier;

    public OTPToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, Customer customer) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.customer = customer;
    }
}
