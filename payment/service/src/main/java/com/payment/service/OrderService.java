package com.payment.service;

import com.load.impl.DataLoad;
import com.payment.common.base.BaseResponse;
import com.payment.entity.dto.OrderCanselDto;
import com.payment.entity.vo.ItemV0;
import com.payment.entity.vo.OrderV0;
import com.payment.entity.dto.ProductItemDto;
import com.payment.entity.vo.ProductItemV0;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    BaseResponse createOrder(Long userId, OrderV0 order);

    BaseResponse getOrder(Long orderId);

    BaseResponse getOrderNo(String orderNo);

    BaseResponse addItem(String orderNo, ProductItemV0 item);

    BaseResponse removeItem(String orderNo, ItemV0 item);

    BaseResponse payment(String orderNo);

    BaseResponse cancel(String orderNo, OrderCanselDto dto);

    BaseResponse submit(String orderNo);

    BaseResponse complete(String orderNo);

    BaseResponse getAllOrder();

    void deleteOutboxRecord();

    BaseResponse getPageable(Pageable pageable);

    BaseResponse getAllLoad(DataLoad load);
}
