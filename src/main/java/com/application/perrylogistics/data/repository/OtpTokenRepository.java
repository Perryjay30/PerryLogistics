package com.application.perrylogistics.data.repository;

import com.application.perrylogistics.data.models.OTPToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpTokenRepository extends MongoRepository<OTPToken, String> {
    Optional<OTPToken> findByToken(String token);

    void deleteOtpTokensByExpiredAtBefore(LocalDateTime currentTime);
}
