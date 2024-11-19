package com.payment.notification.repository;

import com.payment.notification.common.enums.RecordStatus;
import com.payment.notification.entity.model.FirebaseData;
import com.payment.notification.entity.model.FirebaseToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FirebaseTokenRepository extends JpaRepository<FirebaseToken, Long> {

    Optional<FirebaseToken> findBySenderIdAndRecordStatus(String senderId, RecordStatus recordStatus);
}
