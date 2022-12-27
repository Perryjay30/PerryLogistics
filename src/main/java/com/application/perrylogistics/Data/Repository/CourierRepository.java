package com.application.perrylogistics.Data.Repository;

import com.application.perrylogistics.Data.Models.Courier;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourierRepository extends MongoRepository<Courier, String> {
    Optional<Courier> findByEmail(String email);
    Optional<Courier> findByPhoneNumber(String phoneNumber);
}
