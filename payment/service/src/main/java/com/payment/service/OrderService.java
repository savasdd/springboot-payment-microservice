package com.payment.service;

import com.load.impl.DataLoad;
import com.payment.common.base.BaseResponse;
import com.payment.entity.vo.CanselV0;
import com.payment.entity.vo.*;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    BaseResponse createOrder(Long userId, OrderV0 order);

    BaseResponse getOrder(Long orderId);

    BaseResponse getOrderNo(String orderNo);

    BaseResponse addItem(String orderNo, ProductItemV0 item);

    BaseResponse removeItem(String orderNo, ItemV0 item);

    BaseResponse payment(PaymentV0 v0);

    BaseResponse cancel(CanselV0 v0);

    BaseResponse submit(SubmitV0 v0);

    BaseResponse complete(CompleteV0 v0);

    BaseResponse getAllOrder();

    void deleteOutboxRecord();

    BaseResponse getPageable(Pageable pageable);

    BaseResponse getAllLoad(DataLoad load);
}
