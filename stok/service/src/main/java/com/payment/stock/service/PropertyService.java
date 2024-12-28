package com.payment.stock.service;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.PropertyDto;

public interface PropertyService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse save(PropertyDto dto);

    BaseResponse update(PropertyDto dto);

    BaseResponse delete(Long id);

    BaseResponse findAllLoad(DataLoad load);

}
