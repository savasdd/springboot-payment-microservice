package com.payment.notification.common.dto;

import com.payment.notification.common.enums.RecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FirebaseDataRepository extends JpaRepository<FirebaseData, Long> {

    Optional<FirebaseData> findBySenderIdAndRecordStatus(String senderId, RecordStatus recordStatus);
}
