package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.Exceptions.LogisticsException;
import com.application.perrylogistics.Data.Models.Customer;
import com.application.perrylogistics.Data.Models.Order;
import com.application.perrylogistics.Data.Repository.CustomerRepository;
import com.application.perrylogistics.Data.dtos.Request.LoginRequest;
import com.application.perrylogistics.Data.dtos.Request.OrderRequest;
import com.application.perrylogistics.Data.dtos.Request.RegistrationRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateRequest;
import com.application.perrylogistics.Data.dtos.Response.LoginResponse;
import com.application.perrylogistics.Data.dtos.Response.Reciprocation;
import com.application.perrylogistics.Data.dtos.Response.RegistrationResponse;
import com.application.perrylogistics.Validators.UserDetailsValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.application.perrylogistics.Data.Models.PackageCategory.FRAGILE;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderService orderService;
    @Override
    public RegistrationResponse createCustomer(RegistrationRequest customerRegistrationRequest) {
        if(UserDetailsValidators.isValidPassword(customerRegistrationRequest.getPassword()))
            throw new LogisticsException(String.format("password %s is invalid", customerRegistrationRequest.getPassword()));

        if(UserDetailsValidators.isValidEmailAddress(customerRegistrationRequest.getEmail()))
            throw new LogisticsException(String.format("Email address %s is invalid", customerRegistrationRequest.getEmail()));

        if(UserDetailsValidators.isValidPhoneNumber(customerRegistrationRequest.getPhoneNumber()))
            throw new LogisticsException("Please, Enter a valid Phone Number");

        Customer customer = registeringCustomer(customerRegistrationRequest);

        Customer savedCustomer = customerRepository.save(customer);
        return registeredCustomer(savedCustomer);
    }

    private Customer registeringCustomer(RegistrationRequest customerRegistrationRequest) {
        Customer customer = new Customer();
        customer.setFirstName(customerRegistrationRequest.getFirstName());
        customer.setLastName(customerRegistrationRequest.getLastName());
        customer.setEmail(customerRegistrationRequest.getEmail());
        customer.setPassword(customerRegistrationRequest.getPassword());
        customer.setPhoneNumber(customerRegistrationRequest.getPhoneNumber());
        customer.setAddress(customerRegistrationRequest.getAddress());
        return customer;
    }

    private RegistrationResponse registeredCustomer(Customer savedCustomer) {
        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setId(savedCustomer.getId());
        registrationResponse.setStatusCode(201);
        registrationResponse.setMessage("Customer registered successfully");
        return registrationResponse;
    }

    @Override
    public LoginResponse customerLogin(LoginRequest loginRequest) {
        Customer registeredCustomer = customerRepository.findByEmail(loginRequest.getEmail());
        if(registeredCustomer.getEmail().isEmpty())
            throw new LogisticsException("Email cannot be found");

        LoginResponse loginResponse = new LoginResponse();
        if(registeredCustomer.getPassword().equals(loginRequest.getPassword())) {
            loginResponse.setMessage("Login is successful");
            return loginResponse;
        }
        else
            loginResponse.setMessage("Try again, Email or password is incorrect");
        return loginResponse;
    }


    @Override
    public Reciprocation updateCustomer(UpdateRequest updateCustomerRequest) {
        Customer updateCustomer = customerRepository.findById(updateCustomerRequest.getId())
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
    public Reciprocation placeOrder(OrderRequest orderRequest) {
        Customer savedCustomer = customerRepository.findById(orderRequest.getCustomerId())
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
