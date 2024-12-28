package com.payment.stock.repository;

import com.load.base.BaseRepository;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.entity.model.Property;
import com.payment.stock.entity.model.StockRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property>, BaseRepository<Property, Long> {

    Page<Property> findByRecordStatus(RecordStatus status, Pageable pageable);


}
