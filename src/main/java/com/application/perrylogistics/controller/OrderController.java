package com.application.perrylogistics.controller;

import com.application.perrylogistics.data.dtos.request.OrderRequest;
import com.application.perrylogistics.data.dtos.request.UpdateOrderRequest;
import com.application.perrylogistics.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/api/perryLogistics/order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderRequest));
    }

    @GetMapping("/api/perryLogistics/allOrders")
    public ResponseEntity<?> showOrderHistory() {
        return ResponseEntity.ok(orderService.showOrderHistory());
    }

    @PatchMapping("/api/perryLogistics/order/update")
    public ResponseEntity<?> updateOrder(@RequestBody UpdateOrderRequest updateOrderRequest) {
        return ResponseEntity.ok(orderService.updateOrder(updateOrderRequest));
    }

    @DeleteMapping("/api/perryLogistics/order/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
//        log.info("Id -> {}", id);
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

}
