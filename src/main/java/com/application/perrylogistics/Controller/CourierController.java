package com.application.perrylogistics.Controller;

import com.application.perrylogistics.Data.dtos.Request.*;
import com.application.perrylogistics.Service.CourierService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourierController {

    @Autowired
    private CourierService courierService;

    @PostMapping("/api/perryLogistics/courier/createAccount")
    public ResponseEntity<?> createCourier(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courierService.createCourier(verifyOtpRequest));
    }

    @PostMapping("/api/perryLogistics/courier/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest customerRegistrationRequest) {
        return ResponseEntity.
                ok(courierService.register(customerRegistrationRequest));
    }

    @GetMapping("/api/perryLogistics/courier/login")
    public ResponseEntity<?> courierLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(courierService.courierLogin(loginRequest));
    }

    @PostMapping("/api/perryLogistics/courier/changePassword")
    public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(courierService.changePassword(changePasswordRequest));
    }

    @PatchMapping("/api/perryLogistics/courier/update/{id}")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody @PathVariable String id, UpdateRequest updateCustomerRequest) {
        return ResponseEntity.ok(courierService.updateCourier(id, updateCustomerRequest));
    }

    @GetMapping("/api/perryLogistics/courier/forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        return ResponseEntity.ok(courierService.forgotPassword(forgotPasswordRequest));
    }

    @PostMapping("/api/perryLogistics/courier/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(courierService.resetPassword(resetPasswordRequest));
    }

    @DeleteMapping("/api/perryLogistics/courier/delete/{id}")
    public ResponseEntity<?> deleteCourier(@PathVariable String id) {
//        log.info("Id -> {}", id);
       return ResponseEntity.ok(courierService.deleteCourier(id));
    }
}
