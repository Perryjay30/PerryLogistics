package com.application.perrylogistics.service;

import com.application.perrylogistics.data.exceptions.LogisticsException;
import com.application.perrylogistics.data.models.Customer;
import com.application.perrylogistics.data.models.OTPToken;
import com.application.perrylogistics.data.models.Order;
import com.application.perrylogistics.utils.EmailService;
import com.application.perrylogistics.utils.Token;
import com.application.perrylogistics.data.repository.CustomerRepository;
import com.application.perrylogistics.data.repository.OtpTokenRepository;
import com.application.perrylogistics.data.dtos.request.*;
import com.application.perrylogistics.data.dtos.response.LoginResponse;
import com.application.perrylogistics.data.dtos.response.Reciprocation;
import com.application.perrylogistics.data.dtos.response.RegistrationResponse;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.application.perrylogistics.data.models.PackageCategory.FRAGILE;
import static com.application.perrylogistics.data.models.Status.VERIFIED;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderService orderService;
    private final OtpTokenRepository otpTokenRepository;
    private final EmailService emailService;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, OrderService orderService, OtpTokenRepository otpTokenRepository, EmailService emailService) {
        this.customerRepository = customerRepository;
        this.orderService = orderService;
        this.otpTokenRepository = otpTokenRepository;
        this.emailService = emailService;
    }

    @Override
    public RegistrationResponse createAccount(VerifyOtpRequest verifyOtpRequest) {
        verifyOTP(verifyOtpRequest);
        var savedCustomer = customerRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Customer does not exists"));
        savedCustomer.setStatus(VERIFIED);
        customerRepository.save(savedCustomer);
        return registeredCustomerResponse(savedCustomer);
    }

    @Override
    public void verifyOTP(VerifyOtpRequest verifyOtpRequest) {
        OTPToken foundToken = otpTokenRepository.findByToken(verifyOtpRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
        if(foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token has expired");
        if(foundToken.getVerifiedAt() != null)
            throw new RuntimeException("Token has been used");
        if(!Objects.equals(verifyOtpRequest.getToken(), foundToken.getToken()))
            throw new RuntimeException("Incorrect token");
//        otpTokenRepository.setVerifiedAt(LocalDateTime.now(), verifyOtpRequest.getToken());
        var token = otpTokenRepository.findByToken(foundToken.getToken())
                .orElseThrow(()->new RuntimeException("token not found"));
        token.setVerifiedAt(LocalDateTime.now());
        otpTokenRepository.save(token);
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        Customer forgotCustomer = customerRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("This email is not registered"));
        String token = Token.generateToken(4);
        OTPToken otpToken = new OTPToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), forgotCustomer);
        otpTokenRepository.save(otpToken);
        emailService.sendEmail(forgotPasswordRequest.getEmail(), forgotCustomer.getFirstName(), token);
        return "Token successfully sent to your email. Please check.";
    }

    @Override
    public Reciprocation resetPassword(ResetPasswordRequest resetPasswordRequest) {
        verifyOtpForResetPassword(resetPasswordRequest);
        Customer foundCustomer = customerRepository.findByEmail(resetPasswordRequest.getEmail()).get();
        foundCustomer.setPassword(resetPasswordRequest.getPassword());
        if(BCrypt.checkpw(resetPasswordRequest.getConfirmPassword(), resetPasswordRequest.getPassword())) {
            customerRepository.save(foundCustomer);
            return new Reciprocation("Your password has been reset successfully");
        } else {
            throw new IllegalStateException("Password does not match");
        }
    }

    private void verifyOtpForResetPassword(ResetPasswordRequest resetPasswordRequest) {
        var foundToken = otpTokenRepository.findByToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
        if(foundToken.getVerifiedAt() != null)
            throw new RuntimeException("Token has been used");
        if(foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token has expired");
        if(!Objects.equals(resetPasswordRequest.getToken(), foundToken.getToken()))
            throw new RuntimeException("Incorrect token");
        var token = otpTokenRepository.findByToken(foundToken.getToken())
                .orElseThrow(()->new RuntimeException("token not found"));
        token.setVerifiedAt(LocalDateTime.now());
        otpTokenRepository.save(token);
    }

    @Override
    public Reciprocation changePassword(ChangePasswordRequest changePasswordRequest) {
        Customer verifiedCustomer = customerRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("customer isn't registered"));
        if(BCrypt.checkpw(changePasswordRequest.getOldPassword(), verifiedCustomer.getPassword()))
            verifiedCustomer.setPassword(changePasswordRequest.getNewPassword());
        customerRepository.save(verifiedCustomer);
        return new Reciprocation("Your password has been successfully changed");
    }

    @Override
    public String sendOTP(SendOtpRequest sendOtpRequest) {
        Customer savedCustomer = customerRepository.findByEmail(sendOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
        return generateOtpToken(sendOtpRequest, savedCustomer);
    }

    private String generateOtpToken(SendOtpRequest sendOtpRequest, Customer savedCustomer) {
        String token = Token.generateToken(4);
        OTPToken otpToken = new OTPToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), savedCustomer);
        otpTokenRepository.save(otpToken);
        emailService.send(sendOtpRequest.getEmail(), emailService.buildEmail(savedCustomer.getFirstName(), token));
        return "Token successfully sent to your email. Please check.";
    }

    private RegistrationResponse registeredCustomerResponse(Customer savedCustomer) {
        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setId(savedCustomer.getId());
        registrationResponse.setStatusCode(201);
        registrationResponse.setMessage("Customer registered successfully");
        return registrationResponse;
    }

    @Override
    public LoginResponse customerLogin(LoginRequest loginRequest) {
        Customer registeredCustomer = customerRepository.findByEmail(loginRequest.getEmail()).
                orElseThrow(() -> new LogisticsException("Email cannot be found"));

        LoginResponse loginResponse = new LoginResponse();
//        if(registeredCustomer.getPassword().equals(loginRequest.getPassword())) {
        if(BCrypt.checkpw(loginRequest.getPassword(), registeredCustomer.getPassword())) {
            loginResponse.setMessage("Login is successful");
            return loginResponse;
        }
        else
            loginResponse.setMessage("Try again, Email or password is incorrect");
        return loginResponse;
    }


    @Override
    public Reciprocation updateCustomer(String id, UpdateRequest updateCustomerRequest) {
        Customer updateCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new LogisticsException("Customer not found"));
        updatingCustomer(updateCustomerRequest, updateCustomer);
        customerRepository.save(updateCustomer);
        return new Reciprocation("Customer has been updated");
    }

    private void updatingCustomer(UpdateRequest updateCustomerRequest, Customer updateCustomer) {
        updateCustomer.setPassword(updateCustomerRequest.getPassword() != null && !updateCustomerRequest.getPassword()
                .equals("") ? updateCustomerRequest.getPassword() : updateCustomer.getPassword());
        updateCustomer.setEmail(updateCustomerRequest.getEmail() != null && !updateCustomerRequest.getEmail().equals("")
                ? updateCustomerRequest.getEmail() : updateCustomer.getEmail());
        updateCustomer.setAddress(updateCustomerRequest.getAddress() != null && !updateCustomerRequest.getAddress()
                .equals("") ? updateCustomerRequest.getAddress() : updateCustomer.getAddress());
        stillUpdatingCustomer(updateCustomerRequest, updateCustomer);
    }

    private void stillUpdatingCustomer(UpdateRequest updateCustomerRequest, Customer updateCustomer) {
        updateCustomer.setFirstName(updateCustomerRequest.getFirstName() != null && !updateCustomerRequest.getFirstName()
                .equals("") ? updateCustomerRequest.getFirstName() : updateCustomer.getFirstName());
        if(customerRepository.findByPhoneNumber(updateCustomerRequest.getPhone()).isPresent())
            throw new RuntimeException("This Phone Number has been taken, kindly use another");
        else
            updateCustomer.setPhoneNumber(updateCustomerRequest.getPhone() != null && !updateCustomerRequest.getPhone()
                .equals("") ? updateCustomerRequest.getPhone() : updateCustomer.getPhoneNumber());
        updateCustomer.setLastName(updateCustomerRequest.getLastName() != null && !updateCustomerRequest.getLastName()
                .equals("") ? updateCustomerRequest.getLastName() : updateCustomer.getLastName());
    }

    @Override
    public Reciprocation deleteCustomer(String id) {
        customerRepository.deleteById(id);
        return new Reciprocation("Delete successful");
    }

    @Override
    public Reciprocation placeOrder(String id, OrderRequest orderRequest) {
        Customer savedCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Order order = placingAnOrder(orderRequest);
        payingCredentials(orderRequest, order);

        savedCustomer.getCustomerOrders().add(order);
        orderService.createOrder(orderRequest);

        return new Reciprocation("You have placed an order successfully");
    }

    private Order placingAnOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setPackageName(orderRequest.getPackageName());
        order.setPickUpAddress(orderRequest.getPickUpAddress());
        order.setReceiverEmail(orderRequest.getReceiverEmail());
        order.setDestination(orderRequest.getDestination());
        order.setWeight(orderRequest.getWeight());
        order.setReceiverName(orderRequest.getReceiverName());
        order.setReceiverPhoneNumber(orderRequest.getReceiverPhoneNumber());
        return order;
    }

    private void payingCredentials(OrderRequest orderRequest, Order order) {
        order.setReceiverName(orderRequest.getReceiverName());
        order.setPackageCategory(orderRequest.getPackageCategory());
        var placingOrder = order.getPackageCategory().toString();
        if(placingOrder.equalsIgnoreCase(String.valueOf(FRAGILE)))
            order.setAmountToPay(order.getWeight() * order.getPrice());
        else
            order.setAmountToPay(order.getWeight() * order.getPrice() * 0.95);
    }
}
