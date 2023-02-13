package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.dtos.Request.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RegistrationServiceImplTest {

    @Autowired
    private RegistrationService registrationService;


    @Test
    void testThatCustomerCanRegister() {
        RegistrationRequest customerRegistrationRequest = new RegistrationRequest();
        customerRegistrationRequest.setFirstName("Reynold");
        customerRegistrationRequest.setLastName("Bellingham");
        customerRegistrationRequest.setEmail("eybelling@gmail.com");
        customerRegistrationRequest.setPassword("Noldham#97");
        String response =
                registrationService.register(customerRegistrationRequest);
        assertEquals("Token successfully sent to your email. Please check.", response);
    }

    @Test
    void testThatCustomerRegistrationThrowsAnException() {
        RegistrationRequest customerRegistrationRequest = new RegistrationRequest();
        customerRegistrationRequest.setFirstName("Reynold");
        customerRegistrationRequest.setLastName("Bellingham");
        customerRegistrationRequest.setEmail("eybelling@gmail.com");
        customerRegistrationRequest.setPassword("Noldham");
        assertThrows(RuntimeException.class, () -> registrationService.register(customerRegistrationRequest));
    }

}