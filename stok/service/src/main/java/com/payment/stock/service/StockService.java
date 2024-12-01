package com.payment.stock.service;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.StockDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse save(StockDto dto);

    BaseResponse update(StockDto dto);

    BaseResponse delete(Long id);

    BaseResponse updateStockQuantity(Long id, Integer quantity);

    List<StockDto> findAllList(Pageable pageable);

    BaseResponse findPageable(Pageable pageable);

    BaseResponse findAllLoad(DataLoad load);
}
