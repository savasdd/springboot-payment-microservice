package com.payment.stock.service;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.CategoryDto;

public interface CategoryService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse save(CategoryDto dto);

    BaseResponse update(CategoryDto dto);

    BaseResponse delete(Long id);

    BaseResponse findAllLoad(DataLoad load);

}
