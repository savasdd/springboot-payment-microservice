package com.payment.stock.service;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.StockRateDto;

public interface RateService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse save(StockRateDto dto);

    BaseResponse update(StockRateDto dto);

    BaseResponse delete(Long id);

    BaseResponse findAllLoad(DataLoad load);

}
