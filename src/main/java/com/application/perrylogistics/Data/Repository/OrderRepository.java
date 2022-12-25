package com.application.perrylogistics.Data.Repository;

import com.application.perrylogistics.Data.Models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
