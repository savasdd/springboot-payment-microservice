package com.payment.stock.service;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.vo.BasketV0;

public interface BasketService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse save(BasketV0 dto, Long userId);

    BaseResponse delete(Long id, Long userId);

    BaseResponse findAllLoad(DataLoad load, Long userId);

}
