package com.application.perrylogistics.service;

import com.application.perrylogistics.data.dtos.request.*;
import com.application.perrylogistics.data.dtos.response.LoginResponse;
import com.application.perrylogistics.data.dtos.response.Reciprocation;
import com.application.perrylogistics.data.dtos.response.RegistrationResponse;
import jakarta.mail.MessagingException;

public interface CustomerService {

    RegistrationResponse createAccount(VerifyOtpRequest verifyOtpRequest);

    void verifyOTP(VerifyOtpRequest verifyOtpRequest);

    String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;

    Reciprocation resetPassword(ResetPasswordRequest resetPasswordRequest);

    Reciprocation changePassword(ChangePasswordRequest changePasswordRequest);

    String sendOTP(SendOtpRequest sendOtpRequest);

    LoginResponse customerLogin(LoginRequest loginRequest);

    Reciprocation updateCustomer(String id, UpdateRequest updateCustomerRequest);

    Reciprocation deleteCustomer(String id);

    Reciprocation placeOrder(String id, OrderRequest orderRequest);
}
