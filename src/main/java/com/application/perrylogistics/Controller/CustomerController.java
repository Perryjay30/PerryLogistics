package com.application.perrylogistics.Controller;

import com.application.perrylogistics.Data.dtos.Request.*;
import com.application.perrylogistics.Service.CustomerService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @PostMapping("/api/perryLogistics/createAccount")
    public ResponseEntity<?> createAccount(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createAccount(verifyOtpRequest));
    }

    @GetMapping("/api/perryLogistics/customer/login")
    public ResponseEntity<?> customerLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(customerService.customerLogin(loginRequest));
    }

    @GetMapping("/api/perryLogistics/customer/forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        return ResponseEntity.ok(customerService.forgotPassword(forgotPasswordRequest));
    }

    @PostMapping("/api/perryLogistics/customer/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(customerService.resetPassword(resetPasswordRequest));
    }

    @PatchMapping("/api/perryLogistics/customer/update/{id}")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody @PathVariable String id, UpdateRequest updateCustomerRequest) {
        return ResponseEntity.ok(customerService.updateCustomer(id, updateCustomerRequest));
    }

    @PostMapping("/api/perryLogistics/customer/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(customerService.changePassword(changePasswordRequest));
    }

    @DeleteMapping("/api/perryLogistics/customer/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        log.info("Id -> {}", id);
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }



    @PostMapping("/api/perryLogistics/customer/order/{id}")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody @PathVariable String id, OrderRequest orderRequest) {
        return ResponseEntity.ok(customerService.placeOrder(id, orderRequest));
    }
}
