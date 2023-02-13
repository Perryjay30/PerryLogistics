package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.dtos.Request.*;
import com.application.perrylogistics.Data.dtos.Response.LoginResponse;
import com.application.perrylogistics.Data.dtos.Response.Reciprocation;
import com.application.perrylogistics.Data.dtos.Response.RegistrationResponse;
import jakarta.mail.MessagingException;

public interface CourierService {
    String register
            (RegistrationRequest registrationRequest);
    RegistrationResponse createCourier
            (VerifyOtpRequest verifyOtpRequest);

    void verifyOTP(VerifyOtpRequest verifyOtpRequest);

    String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;

    Reciprocation resetPassword(ResetPasswordRequest resetPasswordRequest);

    Reciprocation changePassword(ChangePasswordRequest changePasswordRequest);

    String sendOTP(SendOtpRequest sendOtpRequest);

    LoginResponse courierLogin(LoginRequest loginRequest);

    Reciprocation updateCourier(String id, UpdateRequest updateCourierRequest);

    Reciprocation deleteCourier(String id);
}
