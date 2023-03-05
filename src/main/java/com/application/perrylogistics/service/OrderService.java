package com.application.perrylogistics.service;

import com.application.perrylogistics.data.models.Order;
import com.application.perrylogistics.data.dtos.request.OrderRequest;
import com.application.perrylogistics.data.dtos.request.UpdateOrderRequest;
import com.application.perrylogistics.data.dtos.response.OrderResponse;
import com.application.perrylogistics.data.dtos.response.Reciprocation;

import java.util.List;

public interface OrderService {
OrderResponse createOrder(OrderRequest orderRequest);
List<Order> showOrderHistory();
Reciprocation updateOrder(UpdateOrderRequest updateOrderRequest);
Reciprocation deleteOrder(String id);
void deleteAllOrders();
}
