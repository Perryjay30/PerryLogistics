package com.application.perrylogistics.Data.Repository;

import com.application.perrylogistics.Data.Models.Courier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourierRepository extends MongoRepository<Courier, String> {
    Courier findByEmail(String email);
}
