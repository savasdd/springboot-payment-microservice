package com.payment.stock.repository;

import com.load.base.BaseRepository;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.entity.model.StockRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockRateRepository extends JpaRepository<StockRate, Long>, JpaSpecificationExecutor<StockRate>, BaseRepository<StockRate, Long> {

    Page<StockRate> findByRecordStatus(RecordStatus status, Pageable pageable);


}
