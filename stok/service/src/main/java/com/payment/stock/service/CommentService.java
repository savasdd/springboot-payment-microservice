package com.payment.stock.service;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.vo.CommentV0;

public interface CommentService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse save(CommentV0 dto, Long userId);

    BaseResponse update(CommentV0 dto, Long userId);

    BaseResponse delete(Long id,Long userId);

    BaseResponse findAllLoad(DataLoad load, Long userId);

}
