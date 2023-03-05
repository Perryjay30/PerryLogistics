package com.application.perrylogistics.data.repository;

import com.application.perrylogistics.data.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
