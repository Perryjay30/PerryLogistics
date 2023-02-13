package com.application.perrylogistics.Data.Repository;

import com.application.perrylogistics.Data.Models.OTPToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpTokenRepository extends MongoRepository<OTPToken, String> {
    Optional<OTPToken> findByToken(String token);

    void deleteOtpTokensByExpiredAtBefore(LocalDateTime currentTime);
}
