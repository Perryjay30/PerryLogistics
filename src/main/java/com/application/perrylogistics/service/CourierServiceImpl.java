package com.application.perrylogistics.service;

import com.application.perrylogistics.data.exceptions.LogisticsException;
import com.application.perrylogistics.data.models.Courier;
import com.application.perrylogistics.data.models.OTPToken;
import com.application.perrylogistics.data.repository.CourierRepository;
import com.application.perrylogistics.data.repository.OtpTokenRepository;
import com.application.perrylogistics.data.dtos.request.*;
import com.application.perrylogistics.data.dtos.response.LoginResponse;
import com.application.perrylogistics.data.dtos.response.Reciprocation;
import com.application.perrylogistics.data.dtos.response.RegistrationResponse;
import com.application.perrylogistics.utils.EmailService;
import com.application.perrylogistics.utils.Token;
import com.application.perrylogistics.utils.validators.UserDetailsValidators;
import jakarta.mail.MessagingException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.application.perrylogistics.data.models.Status.VERIFIED;

@Service
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;

    private final OtpTokenRepository otpTokenRepository;

    private final EmailService emailService;

    @Autowired
    public CourierServiceImpl(CourierRepository courierRepository, OtpTokenRepository otpTokenRepository, EmailService emailService) {
        this.courierRepository = courierRepository;
        this.otpTokenRepository = otpTokenRepository;
        this.emailService = emailService;
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
//        if(courierRepository.findByPhoneNumber(courierRegistrationRequest.getPhoneNumber()).isPresent())
//            throw new RuntimeException("This Phone Number has been taken, kindly use another");
//        else
//            courier.setPhoneNumber(courierRegistrationRequest.getPhoneNumber());
//        courier.setAddress(courierRegistrationRequest.getAddress());
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
    public String register(RegistrationRequest registrationRequest) {
        if(!UserDetailsValidators.isValidEmailAddress(registrationRequest.getEmail()))
            throw new LogisticsException(String.format("Email address %s is invalid", registrationRequest.getEmail()));
        Courier courier = registeringCourier(registrationRequest);
        courierRepository.save(courier);
        SendOtpRequest OTPRequest = new SendOtpRequest();
        OTPRequest.setEmail(registrationRequest.getEmail());
        return sendOTP(OTPRequest);
    }

    @Override
    public RegistrationResponse createCourier(VerifyOtpRequest verifyOtpRequest) {
        verifyOTP(verifyOtpRequest);
        var savedCourier = courierRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Courier does not exists"));
        savedCourier.setStatus(VERIFIED);
        courierRepository.save(savedCourier);
        return registeredCourier(savedCourier);
    }

    @Override
    public void verifyOTP(VerifyOtpRequest verifyOtpRequest) {
        var foundToken = getByToken(verifyOtpRequest)
                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
        if(foundToken.getVerifiedAt() != null)
            throw new RuntimeException("Token has been used");
        if(foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token has expired");
        if(!Objects.equals(verifyOtpRequest.getToken(), foundToken.getToken()))
            throw new RuntimeException("Incorrect token");
//        otpTokenRepository.setVerifiedAt(LocalDateTime.now(), verifyOtpRequest.getToken());
        var token = getByToken(verifyOtpRequest)
                .orElseThrow(()->new RuntimeException("token not found"));
        token.setVerifiedAt(LocalDateTime.now());
        otpTokenRepository.save(token);
    }

    private Optional<OTPToken> getByToken(VerifyOtpRequest verifyOtpRequest) {
        return otpTokenRepository.findByToken(verifyOtpRequest.getToken());
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        Courier forgotCourier = courierRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("This email is not registered"));
        String genToken = Token.generateToken(4);
        OTPToken otpToken = new OTPToken();
        otpToken.setToken(genToken);
        otpToken.setCourier(forgotCourier);
        otpToken.setCreatedAt(LocalDateTime.now());
        otpToken.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        otpTokenRepository.save(otpToken);
        emailService.sendEmail(forgotPasswordRequest.getEmail(), forgotCourier.getFirstName(), genToken);
        return "Token successfully sent to your email. Please check.";
    }

    @Override
    public Reciprocation resetPassword(ResetPasswordRequest resetPasswordRequest) {
        verifyOtpForResetPassword(resetPasswordRequest);
        Courier foundCourier = courierRepository.findByEmail(resetPasswordRequest.getEmail()).get();
        foundCourier.setPassword(resetPasswordRequest.getPassword());
        if(BCrypt.checkpw(resetPasswordRequest.getConfirmPassword(), resetPasswordRequest.getPassword())) {
            courierRepository.save(foundCourier);
            return new Reciprocation("Your password has been reset successfully");
        } else {
            throw new IllegalStateException("Password does not match");
        }
    }

    private void verifyOtpForResetPassword(ResetPasswordRequest resetPasswordRequest) {
//        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
        var foundToken = otpTokenRepository.findByToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
        if (!Objects.equals(resetPasswordRequest.getToken(), foundToken.getToken()))
            throw new RuntimeException("Incorrect token");
        if (foundToken.getVerifiedAt() != null)
            throw new RuntimeException("Token has been used");
        if (foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token has expired");
        var token = otpTokenRepository.findByToken(foundToken.getToken())
                .orElseThrow(() -> new RuntimeException("token not found"));
        token.setVerifiedAt(LocalDateTime.now());
        otpTokenRepository.save(token);
    }

    @Override
    public Reciprocation changePassword(ChangePasswordRequest changePasswordRequest) {
        Courier verifiedCourier = courierRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("courier isn't registered"));
        if(BCrypt.checkpw(changePasswordRequest.getOldPassword(), verifiedCourier.getPassword()))
            verifiedCourier.setPassword(changePasswordRequest.getNewPassword());
        courierRepository.save(verifiedCourier);
        return new Reciprocation("Your password has been successfully changed");
    }

    @Override
    public String sendOTP(SendOtpRequest sendOtpRequest) {
        Courier savedCourier = courierRepository.findByEmail(sendOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
        return generateOtpToken(sendOtpRequest, savedCourier);
    }

    private String generateOtpToken(SendOtpRequest sendOtpRequest, Courier savedCourier) {
        String genToken = Token.generateToken(4);
        OTPToken otpToken = new OTPToken();
        otpToken.setToken(genToken);
        otpToken.setCourier(savedCourier);
        otpToken.setCreatedAt(LocalDateTime.now());
        otpToken.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        otpTokenRepository.save(otpToken);
        emailService.send(sendOtpRequest.getEmail(), emailService.buildEmail(savedCourier.getFirstName(), genToken));
        return "Token successfully sent to your email. Please check.";
    }

    @Override
    public LoginResponse courierLogin(LoginRequest loginRequest) {
        Courier registeredCourier = courierRepository.findByEmail(loginRequest.getEmail()).
                orElseThrow(() -> new RuntimeException("Email cannot be found"));

        LoginResponse loginResponse = new LoginResponse();
//        if(registeredCourier.getPassword().equals(loginRequest.getPassword())) {
        if(BCrypt.checkpw(loginRequest.getPassword(), registeredCourier.getPassword())) {
            loginResponse.setMessage("Login is successful");
            return loginResponse;
        }
        else
            loginResponse.setMessage("Try again, Email or password is incorrect");
        return loginResponse;
    }

    @Override
    public Reciprocation updateCourier(String id, UpdateRequest updateCourierRequest) {
        Courier updateCourier = courierRepository.findById(id)
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
