package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.dtos.Request.LoginRequest;
import com.application.perrylogistics.Data.dtos.Request.RegistrationRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateRequest;
import com.application.perrylogistics.Data.dtos.Response.LoginResponse;
import com.application.perrylogistics.Data.dtos.Response.Reciprocation;
import com.application.perrylogistics.Data.dtos.Response.RegistrationResponse;

public interface CourierService {
    RegistrationResponse createCourier
            (RegistrationRequest courierRegistrationRequest);

    LoginResponse courierLogin(LoginRequest loginRequest);

    Reciprocation updateCourier(UpdateRequest updateCourierRequest);

    Reciprocation deleteCourier(String id);
}
