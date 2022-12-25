package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.Models.PackageCategory;
import com.application.perrylogistics.Data.dtos.Request.LoginRequest;
import com.application.perrylogistics.Data.dtos.Request.OrderRequest;
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
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    private RegistrationRequest customerRegistrationRequest;

    @BeforeEach
    void setUp() {
        customerRegistrationRequest = new RegistrationRequest();
        customerRegistrationRequest.setFirstName("Reynold");
        customerRegistrationRequest.setLastName("Bellingham");
        customerRegistrationRequest.setAddress("141, Thompson Highway, Arizona");
        customerRegistrationRequest.setEmail("eybelling@gmail.com");
        customerRegistrationRequest.setPassword("Noldham#97");
        customerRegistrationRequest.setPhoneNumber("09153219609");
    }

    @Test
    void testThatCustomerCanRegister() {
        RegistrationResponse customerRegistrationResponse =
                customerService.createCustomer(customerRegistrationRequest);
        assertNotNull(customerRegistrationResponse);
        assertEquals("Customer registered successfully",
                customerRegistrationResponse.getMessage());
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
    void testThatCustomerDetailsCanBeUpdated() {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setId("63a4fd9a42ac431507e6dbfa");
        updateRequest.setEmail("bellrey@gmail.com");
        updateRequest.setPhone("07021458930");
        updateRequest.setFirstName("Jude");
        Reciprocation reply = customerService.updateCustomer(updateRequest);
        assertEquals("Customer has been updated", reply.getMessage());
    }

    @Test
    void testThatCustomerCanBeDeleted() {
        Reciprocation deleteReply =
                customerService.deleteCustomer("63a33b5331a8a97d85a87d95");
        assertEquals("Delete successful", deleteReply.getMessage());
    }

    @Test
    void testThatCustomerCanPlaceOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId("63a843866d0b715d70db63db");
        orderRequest.setReceiverName("Steph Curry");
        orderRequest.setReceiverEmail("stephCurrie@gmail.com");
        orderRequest.setPackageCategory(PackageCategory.FRAGILE);
        orderRequest.setWeight(27.18);
        orderRequest.setPickUpAddress("101, Old Church Road, LA, USA");
        orderRequest.setDestination("34, Washington Street, Washington DC, USA");
        orderRequest.setReceiverPhoneNumber("041-6758-540");
        Reciprocation response = customerService.placeOrder(orderRequest);
        assertEquals("You have placed an order successfully", response.getMessage());
    }
}