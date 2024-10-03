package com.payment.stock.repository;

import com.payment.stock.entity.model.CDNData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CDNRepository extends JpaRepository<CDNData, Long> {

    @Query(value = "select v from CDNData v where v.recordStatus=com.payment.stock.common.enums.RecordStatus.ACTIVE")
    Optional<CDNData> getActiveCDN();
}
