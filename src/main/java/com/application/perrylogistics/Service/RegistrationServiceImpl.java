package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.Exceptions.LogisticsException;
import com.application.perrylogistics.Data.Models.Customer;
import com.application.perrylogistics.Data.Repository.CustomerRepository;
import com.application.perrylogistics.Data.dtos.Request.RegistrationRequest;
import com.application.perrylogistics.Data.dtos.Request.SendOtpRequest;
import com.application.perrylogistics.utils.Validators.UserDetailsValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.application.perrylogistics.Data.Models.Status.UNVERIFIED;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    @Autowired
    public RegistrationServiceImpl(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }


    @Override
    public String register(RegistrationRequest registrationRequest) {
        if(!UserDetailsValidators.isValidEmailAddress(registrationRequest.getEmail()))
            throw new LogisticsException(String.format("Email address %s is invalid", registrationRequest.getEmail()));

//        if(UserDetailsValidators.isValidPhoneNumber(registrationRequest.getPhoneNumber()))
//            throw new LogisticsException("Please, Enter a valid Phone Number");

        Customer customer = registeringCustomer(registrationRequest);
        customerRepository.save(customer);
        SendOtpRequest sendOtpRequest = new SendOtpRequest();
        sendOtpRequest.setEmail(registrationRequest.getEmail());
        return customerService.sendOTP(sendOtpRequest);
    }

    private Customer registeringCustomer(RegistrationRequest customerRegistrationRequest) {
        Customer customer = new Customer();
        customer.setFirstName(customerRegistrationRequest.getFirstName());
        customer.setLastName(customerRegistrationRequest.getLastName());
        if(customerRepository.findByEmail(customerRegistrationRequest.getEmail()).isPresent())
            throw new RuntimeException("This email has been taken, kindly register with another email address");
        else
            customer.setEmail(customerRegistrationRequest.getEmail());
        customer.setPassword(customerRegistrationRequest.getPassword());
        customer.setStatus(UNVERIFIED);
        return customer;
    }
}
