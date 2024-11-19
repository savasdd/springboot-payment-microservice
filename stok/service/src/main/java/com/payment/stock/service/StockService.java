package com.payment.stock.service;

import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.StockDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockService {
    BaseResponse findAll(Pageable pageable);

    BaseResponse findById(Long id);

    BaseResponse save(StockDto dto);

    BaseResponse update(Long id, StockDto dto);

    BaseResponse delete(Long id);

    BaseResponse updateStockQuantity(Long id, Integer quantity);

    List<StockDto> findAllList(Pageable pageable);
}
