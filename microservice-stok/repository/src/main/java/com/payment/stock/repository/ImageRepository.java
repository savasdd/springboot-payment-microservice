package com.payment.stock.repository;

import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.entity.dto.ImageInfoDto;
import com.payment.stock.entity.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByStock_IdAndRecordStatus(Long id, RecordStatus recordStatus);

    @Query(value = "select new com.payment.stock.entity.dto.ImageInfoDto(v.stock.id,v.name) from Image v where v.recordStatus=:status")
    List<ImageInfoDto> getAllImage(RecordStatus status);
}
