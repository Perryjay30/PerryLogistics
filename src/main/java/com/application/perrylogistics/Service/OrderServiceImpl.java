package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.Exceptions.LogisticsException;
import com.application.perrylogistics.Data.Models.Order;
import com.application.perrylogistics.Data.Repository.OrderRepository;
import com.application.perrylogistics.Data.dtos.Request.OrderRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateOrderRequest;
import com.application.perrylogistics.Data.dtos.Response.OrderResponse;
import com.application.perrylogistics.Data.dtos.Response.Reciprocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.application.perrylogistics.Data.Models.PackageCategory.FRAGILE;



@Service
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = placingOrder(orderRequest);
        Order savedOrder = orderRepository.save(order);

        return placedOrderResponse(savedOrder);
    }

    private OrderResponse placedOrderResponse(Order savedOrder) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(savedOrder.getId());
        orderResponse.setStatusCode(201);
        orderResponse.setMessage("You just made a successful order");
        return orderResponse;
    }

    private Order placingOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setPackageName(orderRequest.getPackageName());
        order.setReceiverName(orderRequest.getReceiverName());
        order.setReceiverPhoneNumber(orderRequest.getReceiverPhoneNumber());
        order.setReceiverEmail(orderRequest.getReceiverEmail());
        order.setPickUpAddress(orderRequest.getPickUpAddress());
        order.setDestination(orderRequest.getDestination());
        paymentCredentials(orderRequest, order);
        return order;
    }
    private void paymentCredentials(OrderRequest orderRequest, Order order) {
        order.setWeight(orderRequest.getWeight());
        order.setPackageCategory(orderRequest.getPackageCategory());
        var placingOrder = order.getPackageCategory().toString();
        if(placingOrder.equalsIgnoreCase(String.valueOf(FRAGILE)))
            order.setAmountToPay(order.getWeight() * order.getPrice());
        else
            order.setAmountToPay(order.getWeight() * order.getPrice() * 0.95);
    }


    @Override
    public List<Order> showOrderHistory() {
        return orderRepository.findAll();
    }

    @Override
    public Reciprocation updateOrder(UpdateOrderRequest updateOrderRequest) {
        Order foundOrder = orderRepository.findById(updateOrderRequest.getId())
                .orElseThrow(() -> new LogisticsException("Order cannot be found"));
        updatingOrder(updateOrderRequest, foundOrder);
        foundOrder.setPickUpAddress(updateOrderRequest.getPickUpAddress() != null && !updateOrderRequest.getPickUpAddress()
                .equals("") ? updateOrderRequest.getPickUpAddress() : foundOrder.getPickUpAddress());
        foundOrder.setReceiverPhoneNumber(updateOrderRequest.getReceiverPhoneNumber() != null && !updateOrderRequest.getReceiverPhoneNumber()
                .equals("") ? updateOrderRequest.getReceiverPhoneNumber() : foundOrder.getReceiverPhoneNumber());
        orderRepository.save(foundOrder);
        return new Reciprocation("Your order has been updated, kindly replace order!!");
    }

    private void updatingOrder(UpdateOrderRequest updateOrderRequest, Order foundOrder) {
        foundOrder.setDestination(updateOrderRequest.getDestination() != null && !updateOrderRequest.getDestination()
                .equals("") ? updateOrderRequest.getDestination() : foundOrder.getDestination());
        foundOrder.setPackageName(updateOrderRequest.getPackageName() != null && !updateOrderRequest.getPackageName()
                .equals("") ? updateOrderRequest.getPackageName() : foundOrder.getPackageName());
        foundOrder.setReceiverEmail(updateOrderRequest.getReceiverEmail()!= null && !updateOrderRequest.getReceiverEmail()
                .equals("") ? updateOrderRequest.getReceiverEmail() : foundOrder.getReceiverEmail());
        foundOrder.setReceiverName(updateOrderRequest.getReceiverName() != null && !updateOrderRequest.getReceiverName()
                .equals("") ? updateOrderRequest.getReceiverName() : foundOrder.getReceiverName());
        updatingPaymentCredentials(updateOrderRequest, foundOrder);
    }

    private void updatingPaymentCredentials(UpdateOrderRequest updateOrderRequest, Order foundOrder) {
        foundOrder.setWeight(updateOrderRequest.getWeight() != 0.00
                ? updateOrderRequest.getWeight() : foundOrder.getWeight());
        foundOrder.setPackageCategory(updateOrderRequest.getPackageCategory());
        var updatingPlacedOrder = foundOrder.getPackageCategory().toString();
        if(updatingPlacedOrder.equalsIgnoreCase(String.valueOf(FRAGILE)))
            foundOrder.setAmountToPay(foundOrder.getWeight() * foundOrder.getPrice());
        else
            foundOrder.setAmountToPay(foundOrder.getWeight() * foundOrder.getPrice() * 0.95);
    }

    @Override
    public Reciprocation deleteOrder(String id) {
        orderRepository.deleteById(id);
        return new Reciprocation("Order deleted");
    }

    @Override
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}
