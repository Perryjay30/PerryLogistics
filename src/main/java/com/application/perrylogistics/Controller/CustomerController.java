package com.application.perrylogistics.Controller;

import com.application.perrylogistics.Data.dtos.Request.LoginRequest;
import com.application.perrylogistics.Data.dtos.Request.OrderRequest;
import com.application.perrylogistics.Data.dtos.Request.RegistrationRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateRequest;
import com.application.perrylogistics.Service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/api/perryLogistics/customer")
    public ResponseEntity<?> createCustomer(@RequestBody RegistrationRequest customerRegistrationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).
                body(customerService.createCustomer(customerRegistrationRequest));
    }

    @GetMapping("/api/perryLogistics/customer/login")
    public ResponseEntity<?> customerLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(customerService.customerLogin(loginRequest));
    }

    @PatchMapping("/api/perryLogistics/customer/update")
    public ResponseEntity<?> updateCustomer(@RequestBody UpdateRequest updateCustomerRequest) {
        return ResponseEntity.ok(customerService.updateCustomer(updateCustomerRequest));
    }

    @DeleteMapping("/api/perryLogistics/customer/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        log.info("Id -> {}", id);
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }

    @PostMapping("/api/perryLogistics/customer/order")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(customerService.placeOrder(orderRequest));
    }
}
