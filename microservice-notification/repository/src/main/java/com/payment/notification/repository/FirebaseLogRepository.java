package com.payment.notification.repository;

import com.payment.notification.entity.model.FirebaseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirebaseLogRepository extends JpaRepository<FirebaseLog, Long> {

}
