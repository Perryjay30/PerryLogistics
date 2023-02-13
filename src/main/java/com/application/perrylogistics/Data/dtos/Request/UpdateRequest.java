package com.application.perrylogistics.Data.dtos.Request;

import com.application.perrylogistics.utils.Validators.UserDetailsValidators;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRequest {
    @NotBlank(message = "This field is required")
    private String phone;
    @NotBlank(message = "This field is required")
    private String firstName;
    @NotBlank(message = "This field is required")
    private String lastName;
    @NotBlank(message = "This field is required")
    private String email;
    private String password;
    @NotBlank(message = "This field is required")
    private String address;

    public String getPhone() {
        if(!UserDetailsValidators.isValidPhoneNumber(phone)) {
            throw new RuntimeException("Phone number must be a length of 11");
        }
        return phone;
    }


}
