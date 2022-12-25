package com.application.perrylogistics.Controller;

import com.application.perrylogistics.Data.dtos.Request.LoginRequest;
import com.application.perrylogistics.Data.dtos.Request.RegistrationRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateRequest;
import com.application.perrylogistics.Service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourierController {

    @Autowired
    private CourierService courierService;

    @PostMapping("/api/perryLogistics/courier")
    public ResponseEntity<?> createCourier(@RequestBody RegistrationRequest courierRegistrationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courierService.createCourier(courierRegistrationRequest));
    }

    @GetMapping("/api/perryLogistics/courier/login")
    public ResponseEntity<?> courierLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(courierService.courierLogin(loginRequest));
    }

    @PatchMapping("/api/perryLogistics/courier/update")
    public ResponseEntity<?> updateCourier(@RequestBody UpdateRequest updateCourierRequest) {
        return ResponseEntity.ok(courierService.updateCourier(updateCourierRequest));
    }

    @DeleteMapping("/api/perryLogistics/courier/delete/{id}")
    public ResponseEntity<?> deleteCourier(@PathVariable String id) {
//        log.info("Id -> {}", id);
       return ResponseEntity.ok(courierService.deleteCourier(id));
    }
}
