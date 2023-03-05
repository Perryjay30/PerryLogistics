package com.application.perrylogistics.data.dtos.request;

import com.application.perrylogistics.utils.validators.UserDetailsValidators;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

@Data
public class RegistrationRequest {
    @NotBlank(message="This field is required")
    private String firstName;
    @NotBlank(message="This field is required")
    private String LastName;
    @Email
    @NotBlank(message="This field is required")
    private String email;
    @NotBlank(message="This field is required")
    private String password;


    public String getPassword() {
        if(UserDetailsValidators.isValidPassword(password))
            return BCrypt.hashpw(password, BCrypt.gensalt());
        else
            throw new RuntimeException("password is invalid");
    }
}
