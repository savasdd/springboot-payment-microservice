package com.payment.repository;

import com.load.base.BaseRepository;
import com.payment.entity.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order>, BaseRepository<Order, Long> {

    Optional<Order> findByOrderNo(String orderNo);
}
