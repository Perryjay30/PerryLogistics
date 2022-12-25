package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.dtos.Request.LoginRequest;
import com.application.perrylogistics.Data.dtos.Request.RegistrationRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateRequest;
import com.application.perrylogistics.Data.dtos.Response.LoginResponse;
import com.application.perrylogistics.Data.dtos.Response.Reciprocation;
import com.application.perrylogistics.Data.dtos.Response.RegistrationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourierServiceImplTest {

    @Autowired
    private CourierService courierService;

    private RegistrationRequest courierRegistrationRequest;

    @BeforeEach
    void setUp() {
        courierRegistrationRequest = new RegistrationRequest();
        courierRegistrationRequest.setFirstName("MacAllister");
        courierRegistrationRequest.setLastName("Spenser");
        courierRegistrationRequest.setAddress("275, Octavio Crescent, Alabama");
        courierRegistrationRequest.setEmail("macSpenser@gmail.com");
        courierRegistrationRequest.setPassword("Wilnock@23");
        courierRegistrationRequest.setPhoneNumber("09164314587");
    }

    @Test
    void testThatCourierCanRegister() {
        RegistrationResponse customerRegistrationResponse =
                courierService.createCourier(courierRegistrationRequest);
        assertNotNull(customerRegistrationResponse);
        assertEquals("Courier registered successfully",
                customerRegistrationResponse.getMessage());
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
    void testThatCourierCanBeUpdated() {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setId("63a345140115de3a66d037c3");
        updateRequest.setEmail("nightingale@gmail.com");
        updateRequest.setPhone("07098673324");
        updateRequest.setFirstName("Trossard");
        Reciprocation reply = courierService.updateCourier(updateRequest);
        assertEquals("Courier has been updated", reply.getMessage());
    }

    @Test
    void testThatCourierCanBeDeleted() {
        Reciprocation deleteReply =
                courierService.deleteCourier("63a345140115de3a66d037c3");
        assertEquals("Courier deleted", deleteReply.getMessage());
    }
}