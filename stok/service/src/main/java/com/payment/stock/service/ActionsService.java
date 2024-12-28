package com.payment.stock.service;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.vo.ActionsV0;

public interface ActionsService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse save(ActionsV0 dto);

    BaseResponse update(ActionsV0 dto);

    BaseResponse delete(Long id);

    BaseResponse findAllLoad(DataLoad load);

}
