package com.application.perrylogistics.data.repository;

import com.application.perrylogistics.data.models.Courier;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourierRepository extends MongoRepository<Courier, String> {
    Optional<Courier> findByEmail(String email);
    Optional<Courier> findByPhoneNumber(String phoneNumber);
}
