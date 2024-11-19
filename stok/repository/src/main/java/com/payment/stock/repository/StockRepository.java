package com.payment.stock.repository;

import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.entity.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long>, JpaSpecificationExecutor<Stock> {

    Page<Stock> findByRecordStatus(RecordStatus status, Pageable pageable);

    @Query(value = "select s from Stock s join fetch s.details d where d.language IN :languages and s.recordStatus =:status and d.recordStatus =:status",
    countQuery = "select count(s) from Stock s join s.details d where d.language IN :languages and s.recordStatus =:status and d.recordStatus =:status")
    Page<Stock> findAllStockByStatus(RecordStatus status, List<String> languages, Pageable pageable);


}
