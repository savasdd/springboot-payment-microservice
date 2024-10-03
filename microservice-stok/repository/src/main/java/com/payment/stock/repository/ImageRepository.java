package com.payment.stock.repository;

import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.entity.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByStock_IdAndRecordStatus(Long id, RecordStatus recordStatus);
}
