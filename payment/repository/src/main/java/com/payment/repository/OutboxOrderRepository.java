package com.payment.repository;

import com.payment.entity.model.OutboxOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OutboxOrderRepository extends JpaRepository<OutboxOrder, Long>, JpaSpecificationExecutor<OutboxOrder> {

    @Modifying
    @Query(value = "delete from outbox_orders where id in(SELECT id FROM outbox_orders ORDER BY cr_date asc LIMIT 10 FOR UPDATE SKIP locked)",nativeQuery = true)
    void deleteOutboxRecordByLimit();
}
