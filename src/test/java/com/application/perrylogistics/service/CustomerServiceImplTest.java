package com.application.perrylogistics.service;

import com.application.perrylogistics.data.models.PackageCategory;
import com.application.perrylogistics.data.dtos.request.*;
import com.application.perrylogistics.data.dtos.response.LoginResponse;
import com.application.perrylogistics.data.dtos.response.Reciprocation;
import com.application.perrylogistics.data.dtos.response.RegistrationResponse;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void testThatCustomerAccountHasBeenCreated() {
        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
        verifyOtpRequest.setToken("0466");
        verifyOtpRequest.setEmail("eybelling@gmail.com");
        RegistrationResponse registrationResponse =
                customerService.createAccount(verifyOtpRequest);
        assertEquals("Customer registered successfully", registrationResponse.getMessage());
    }

    @Test
    void testThatCustomerCanLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("eybelling@gmail.com");
        loginRequest.setPassword("Noldham#97");
        LoginResponse loginResponse = customerService.customerLogin(loginRequest);
        assertEquals("Login is successful", loginResponse.getMessage());
    }

    @Test
    void testThatCustomerCanChangePassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setEmail("eybelling@gmail.com");
        changePasswordRequest.setOldPassword("Noldham#97");
        changePasswordRequest.setNewPassword("Country@65");
        Reciprocation resp = customerService.changePassword(changePasswordRequest);
        assertEquals("Your password has been successfully changed", resp.getMessage());
    }

    @Test
    void testThatForgotPasswordMethodWorks() throws MessagingException {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("eybelling@gmail.com");
        var response = customerService.forgotPassword(forgotPasswordRequest);
        assertEquals("Token successfully sent to your email. Please check.", response);
    }

    @Test
    void testThatPasswordCanBeResetAfterForgotten() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setToken("5829");
        resetPasswordRequest.setEmail("eybelling@gmail.com");
        resetPasswordRequest.setPassword("Thewhitecalf#89");
        resetPasswordRequest.setConfirmPassword("Thewhitecalf#89");
        Reciprocation answer = customerService.resetPassword(resetPasswordRequest);
        assertEquals("Your password has been reset successfully", answer.getMessage());
    }

    @Test
    void testThatCustomerDetailsCanBeUpdated() {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setEmail("bellrey@gmail.com");
        updateRequest.setPhone("+111 (202) 555-0125");
        updateRequest.setFirstName("Jude");
        updateRequest.setAddress("330, Christian road, Ontario");
        Reciprocation reply = customerService.updateCustomer("63e8f10d8332c9473944ce6a", updateRequest);
        assertEquals("Customer has been updated", reply.getMessage());
    }

    @Test
    void testThatCustomerCanBeDeleted() {
        Reciprocation deleteReply =
                customerService.deleteCustomer("63e8f10d8332c9473944ce6a");
        assertEquals("Delete successful", deleteReply.getMessage());
    }

    @Test
    void testThatCustomerCanPlaceOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setReceiverName("Steph Curry");
        orderRequest.setReceiverEmail("stephCurrie@gmail.com");
        orderRequest.setPackageCategory(PackageCategory.FRAGILE);
        orderRequest.setWeight(27.18);
        orderRequest.setPickUpAddress("101, Old Church Road, LA, USA");
        orderRequest.setDestination("34, Washington Street, Washington DC, USA");
        orderRequest.setReceiverPhoneNumber("+234 (083) 921-0482");
        Reciprocation response = customerService.placeOrder("63e8f10d8332c9473944ce6a", orderRequest);
        assertEquals("You have placed an order successfully", response.getMessage());
    }
}