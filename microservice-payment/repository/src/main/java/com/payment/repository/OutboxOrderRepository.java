package com.payment.repository;

import com.payment.entity.model.OutboxOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OutboxOrderRepository extends JpaRepository<OutboxOrder, String>, JpaSpecificationExecutor<OutboxOrder> {
}
