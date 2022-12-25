package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.dtos.Request.OrderRequest;
import com.application.perrylogistics.Data.dtos.Request.RegistrationRequest;
import com.application.perrylogistics.Data.dtos.Request.LoginRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateRequest;
import com.application.perrylogistics.Data.dtos.Response.LoginResponse;
import com.application.perrylogistics.Data.dtos.Response.Reciprocation;
import com.application.perrylogistics.Data.dtos.Response.RegistrationResponse;

public interface CustomerService {
    RegistrationResponse createCustomer
            (RegistrationRequest customerRegistrationRequest);

    LoginResponse customerLogin(LoginRequest loginRequest);

    Reciprocation updateCustomer(UpdateRequest updateCustomerRequest);

    Reciprocation deleteCustomer(String id);

    Reciprocation placeOrder(OrderRequest orderRequest);
}
