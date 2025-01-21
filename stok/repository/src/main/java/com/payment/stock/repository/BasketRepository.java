package com.payment.stock.repository;

import com.load.base.BaseRepository;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.entity.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long>, JpaSpecificationExecutor<Basket>, BaseRepository<Basket, Long> {

    Optional<Basket> findByIdAndUserIdAndRecordStatus(Long id, Long userId, RecordStatus status);

    List<Basket> findAllByIdInAndRecordStatus(List<Long> ids, RecordStatus status);

}
