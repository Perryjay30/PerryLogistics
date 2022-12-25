package com.application.perrylogistics.Service;

import com.application.perrylogistics.Data.Models.PackageCategory;
import com.application.perrylogistics.Data.dtos.Request.OrderRequest;
import com.application.perrylogistics.Data.dtos.Request.UpdateOrderRequest;
import com.application.perrylogistics.Data.dtos.Response.OrderResponse;
import com.application.perrylogistics.Data.dtos.Response.Reciprocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest();
        orderRequest.setPackageName("Iphone 14pro");
        orderRequest.setDestination("No 42, Ayodele Street, Akure. Ondo state");
        orderRequest.setPickUpAddress("No 36, Iwaya road, Yaba, Lagos State");
        orderRequest.setPackageCategory(PackageCategory.FRAGILE);
        orderRequest.setReceiverEmail("tejuosho@gmail.com");
        orderRequest.setReceiverName("Raymond Queen");
        orderRequest.setReceiverPhoneNumber("08146127502");
        orderRequest.setWeight(58.23);
    }

    @Test
    void testThatOrderCanBePlaced() {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        assertEquals("You just made a successful order", orderResponse.getMessage());
    }

    @Test
    void testThatOrderHistoryCanBeRetrieved() {
        orderService.showOrderHistory();
    }

    @Test
    void testThatOrderCanBeUpdated() {
        UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();
        updateOrderRequest.setId("63a5048646798a6ec84a6150");
        updateOrderRequest.setReceiverEmail("michealOkpoyi@gmail.com");
        updateOrderRequest.setDestination("123, Downhill road, sydney, Australia");
        updateOrderRequest.setPackageName("Mercedes E-Class");
        updateOrderRequest.setReceiverName("Micheal Angelo");
        updateOrderRequest.setPackageCategory(PackageCategory.NONFRAGILE);
        Reciprocation reply = orderService.updateOrder(updateOrderRequest);
        assertEquals("Your order has been updated, kindly replace order!!", reply.getMessage());
    }

    @Test
    void testThatOrderCanBeDeleted() {
        Reciprocation reply = orderService.deleteOrder("63a4ffa02e5fab6bc242b993");
        assertEquals("Order deleted", reply.getMessage());
    }
}