package com.payment.stock.repository;

import com.payment.stock.entity.model.StockDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockDetailRepository extends JpaRepository<StockDetail, Long>, JpaSpecificationExecutor<StockDetail> {

}
