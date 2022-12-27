package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.Exceptions.LogisticsException;
import com.application.perrylogistics.Data.Models.Courier;
import com.application.perrylogistics.Data.Repository.CourierRepository;
import com.application.perrylogistics.Data.dtos.Request.LoginRequest;
import com.application.perrylogistics.Data.dtos.Request.RegistrationRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateRequest;
import com.application.perrylogistics.Data.dtos.Response.LoginResponse;
import com.application.perrylogistics.Data.dtos.Response.Reciprocation;
import com.application.perrylogistics.Data.dtos.Response.RegistrationResponse;
import com.application.perrylogistics.Validators.UserDetailsValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Override
    public RegistrationResponse createCourier(RegistrationRequest courierRegistrationRequest) {

        if(UserDetailsValidators.isValidEmailAddress(courierRegistrationRequest.getEmail()))
            throw new LogisticsException(String.format("Email address %s is invalid", courierRegistrationRequest.getEmail()));

        if(UserDetailsValidators.isValidPassword(courierRegistrationRequest.getPassword()))
            throw new LogisticsException(String.format("password %s is invalid", courierRegistrationRequest.getPassword()));

        if(UserDetailsValidators.isValidPhoneNumber(courierRegistrationRequest.getPhoneNumber()))
            throw new LogisticsException("Please, Enter a valid Phone Number");

        Courier courier = registeringCourier(courierRegistrationRequest);

        Courier savedCourier = courierRepository.save(courier);
        return registeredCourier(savedCourier);
    }

    private Courier registeringCourier(RegistrationRequest courierRegistrationRequest) {
        Courier courier = new Courier();
        courier.setLastName(courierRegistrationRequest.getLastName());
        if(courierRepository.findByEmail(courierRegistrationRequest.getEmail()).isPresent())
            throw new RuntimeException("This email has been taken, kindly register with another email address");
        else
            courier.setEmail(courierRegistrationRequest.getEmail());
        courier.setFirstName(courierRegistrationRequest.getFirstName());
        courier.setPassword(courierRegistrationRequest.getPassword());
        if(courierRepository.findByPhoneNumber(courierRegistrationRequest.getPhoneNumber()).isPresent())
            throw new RuntimeException("This Phone Number has been taken, kindly use another");
        else
            courier.setPhoneNumber(courierRegistrationRequest.getPhoneNumber());
        courier.setAddress(courierRegistrationRequest.getAddress());
        return courier;
    }

    private RegistrationResponse registeredCourier(Courier savedCourier) {
        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setId(savedCourier.getId());
        registrationResponse.setStatusCode(201);
        registrationResponse.setMessage("Courier registered successfully");
        return registrationResponse;
    }


    @Override
    public LoginResponse courierLogin(LoginRequest loginRequest) {
        Courier registeredCourier = courierRepository.findByEmail(loginRequest.getEmail()).
                orElseThrow(() -> new RuntimeException("Email cannot be found"));

        LoginResponse loginResponse = new LoginResponse();
        if(registeredCourier.getPassword().equals(loginRequest.getPassword())) {
            loginResponse.setMessage("Login is successful");
            return loginResponse;
        }
        else
            loginResponse.setMessage("Try again, Email or password is incorrect");
        return loginResponse;
    }

    @Override
    public Reciprocation updateCourier(UpdateRequest updateCourierRequest) {
        Courier updateCourier = courierRepository.findById(updateCourierRequest.getId())
                .orElseThrow(() -> new LogisticsException("Courier not found"));
        updatingCustomer(updateCourierRequest, updateCourier);
        courierRepository.save(updateCourier);
        return new Reciprocation("Courier has been updated");
    }

    private void updatingCustomer(UpdateRequest updateCourierRequest, Courier updateCourier) {
        updateCourier.setEmail(updateCourierRequest.getEmail() != null && !updateCourierRequest.getEmail().equals("")
                ? updateCourierRequest.getEmail() : updateCourier.getEmail());
        updateCourier.setPassword(updateCourierRequest.getPassword() != null && !updateCourierRequest.getPassword()
                .equals("") ? updateCourierRequest.getPassword() : updateCourier.getPassword());
        updateCourier.setAddress(updateCourierRequest.getAddress() != null && !updateCourierRequest.getAddress()
                .equals("") ? updateCourierRequest.getAddress() : updateCourier.getAddress());
        stillUpdatingCustomer(updateCourierRequest, updateCourier);
    }

    private void stillUpdatingCustomer(UpdateRequest updateCourierRequest, Courier updateCourier) {
        updateCourier.setPhoneNumber(updateCourierRequest.getPhone() != null && !updateCourierRequest.getPhone()
                .equals("") ? updateCourierRequest.getPhone() : updateCourier.getPhoneNumber());
        updateCourier.setFirstName(updateCourierRequest.getFirstName() != null && !updateCourierRequest.getFirstName()
                .equals("") ? updateCourierRequest.getFirstName() : updateCourier.getFirstName());
        updateCourier.setLastName(updateCourierRequest.getLastName() != null && !updateCourierRequest.getLastName()
                .equals("") ? updateCourierRequest.getLastName() : updateCourier.getLastName());
    }

    @Override
    public Reciprocation deleteCourier(String id) {
        courierRepository.deleteById(id);
        return new Reciprocation("Courier deleted");
    }
}
