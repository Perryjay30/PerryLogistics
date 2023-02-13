package com.application.perrylogistics.Controller;

import com.application.perrylogistics.Data.dtos.Request.RegistrationRequest;
import com.application.perrylogistics.Service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/api/perryLogistics/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest customerRegistrationRequest) {
        return ResponseEntity.
                ok(registrationService.register(customerRegistrationRequest));
    }

}
