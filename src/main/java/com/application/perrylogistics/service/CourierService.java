package com.application.perrylogistics.service;

import com.application.perrylogistics.data.dtos.request.*;
import com.application.perrylogistics.data.dtos.response.LoginResponse;
import com.application.perrylogistics.data.dtos.response.Reciprocation;
import com.application.perrylogistics.data.dtos.response.RegistrationResponse;
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
