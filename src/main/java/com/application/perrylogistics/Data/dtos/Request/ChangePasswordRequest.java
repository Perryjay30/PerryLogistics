package com.application.perrylogistics.Data.dtos.Request;

import com.application.perrylogistics.utils.Validators.UserDetailsValidators;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "This field must not be empty")
    private String email;
    @NotBlank(message = "This field must not be empty")
    private String oldPassword;
    @NotBlank(message = "This field must not be empty")
    private String newPassword;


    public String getNewPassword() {
        if(UserDetailsValidators.isValidPassword(newPassword))
            return BCrypt.hashpw(newPassword, BCrypt.gensalt());
        else
            throw new RuntimeException("password must contain at least one " +
                    "capital letter, small letter and special characters");
    }
}
