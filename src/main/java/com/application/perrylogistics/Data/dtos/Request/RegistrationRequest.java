package com.application.perrylogistics.Data.dtos.Request;

import com.application.perrylogistics.utils.Validators.UserDetailsValidators;
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
