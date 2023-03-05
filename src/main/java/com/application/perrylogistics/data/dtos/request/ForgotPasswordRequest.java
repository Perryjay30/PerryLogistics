package com.application.perrylogistics.data.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "This field must not be empty")
    private String email;
}
