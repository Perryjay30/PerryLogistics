package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.dtos.Request.*;
import com.application.perrylogistics.Data.dtos.Response.LoginResponse;
import com.application.perrylogistics.Data.dtos.Response.Reciprocation;
import com.application.perrylogistics.Data.dtos.Response.RegistrationResponse;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourierServiceImplTest {

    @Autowired
    private CourierService courierService;

    @Test
    void testThatCourierCanRegister() {
        RegistrationRequest courierRegistrationRequest = new RegistrationRequest();
        courierRegistrationRequest.setFirstName("MacAllister");
        courierRegistrationRequest.setLastName("Spenser");
        courierRegistrationRequest.setEmail("macSpenser@gmail.com");
        courierRegistrationRequest.setPassword("Wilnock@23");
        String response =
                courierService.register(courierRegistrationRequest);
        assertEquals("Token successfully sent to your email. Please check.", response);
    }

    @Test
    void testThatCustomerAccountHasBeenCreated() {
        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
        verifyOtpRequest.setToken("4598");
        verifyOtpRequest.setEmail("macSpenser@gmail.com");
        RegistrationResponse registrationResponse =
                courierService.createCourier(verifyOtpRequest);
        assertEquals("Courier registered successfully", registrationResponse.getMessage());
    }

    @Test
    void testThatCourierCanLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("macSpenser@gmail.com");
        loginRequest.setPassword("Wilnock@23");
        LoginResponse loginResponse = courierService.courierLogin(loginRequest);
        assertEquals("Login is successful", loginResponse.getMessage());
    }

    @Test
    void testThatCourierCanChangePassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setEmail("macSpenser@gmail.com");
        changePasswordRequest.setOldPassword("Wilnock@23");
        changePasswordRequest.setNewPassword("Johnwick!00");
        Reciprocation resp = courierService.changePassword(changePasswordRequest);
        assertEquals("Your password has been successfully changed", resp.getMessage());
    }

    @Test
    void testThatForgotPasswordMethodWorks() throws MessagingException {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("macSpenser@gmail.com");
        var response = courierService.forgotPassword(forgotPasswordRequest);
        assertEquals("Token successfully sent to your email. Please check.", response);
    }

    @Test
    void testThatPasswordCanBeResetAfterForgotten() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setToken("9325");
        resetPasswordRequest.setEmail("macSpenser@gmail.com");
        resetPasswordRequest.setPassword("Theblackeagle#41");
        resetPasswordRequest.setConfirmPassword("Theblackeagle#41");
        Reciprocation answer = courierService.resetPassword(resetPasswordRequest);
        assertEquals("Your password has been reset successfully", answer.getMessage());
    }

    @Test
    void testThatCourierCanBeUpdated() {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setEmail("ghostwalker@gmail.com");
        updateRequest.setPhone("+234 (816) 425-9071");
        updateRequest.setFirstName("Trossard");
        Reciprocation reply = courierService.updateCourier("63e9053e667c017340ad91d5", updateRequest);
        assertEquals("Courier has been updated", reply.getMessage());
    }

    @Test
    void testThatCourierCanBeDeleted() {
        Reciprocation deleteReply =
                courierService.deleteCourier("63e9053e667c017340ad91d5");
        assertEquals("Courier deleted", deleteReply.getMessage());
    }
}