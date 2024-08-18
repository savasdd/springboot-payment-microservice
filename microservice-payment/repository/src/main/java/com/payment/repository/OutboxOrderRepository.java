package com.payment.repository;

import com.payment.entity.model.OutboxOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OutboxOrderRepository extends JpaRepository<OutboxOrder, UUID>, JpaSpecificationExecutor<OutboxOrder> {
}
