package com.payment.stock.repository;

import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.entity.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {

    Page<Stock> findByRecordStatus(RecordStatus status, Pageable pageable);


}
