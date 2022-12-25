package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.Models.Order;
import com.application.perrylogistics.Data.dtos.Request.OrderRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateOrderRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateRequest;
import com.application.perrylogistics.Data.dtos.Response.OrderResponse;
import com.application.perrylogistics.Data.dtos.Response.Reciprocation;

import java.util.List;

public interface OrderService {
OrderResponse createOrder(OrderRequest orderRequest);
List<Order> showOrderHistory();
Reciprocation updateOrder(UpdateOrderRequest updateOrderRequest);
Reciprocation deleteOrder(String id);
void deleteAllOrders();
}
