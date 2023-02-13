package com.application.perrylogistics.Data.dtos.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "This field must not be empty")
    private String email;
}
