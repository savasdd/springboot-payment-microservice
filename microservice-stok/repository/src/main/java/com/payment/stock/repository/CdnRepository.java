package com.payment.stock.repository;

import com.payment.stock.entity.model.CdnData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CdnRepository extends JpaRepository<CdnData, Long> {

    @Query(value = "select v from CdnData v where v.recordStatus=com.payment.stock.common.enums.RecordStatus.ACTIVE")
    Optional<CdnData> getActiveCDN();
}
