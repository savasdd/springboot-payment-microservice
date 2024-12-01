package com.payment.service;

import com.load.impl.DataLoad;
import com.payment.common.base.BaseResponse;
import com.payment.entity.dto.OrderCanselDto;
import com.payment.entity.dto.OrderDto;
import com.payment.entity.dto.OrderV0;
import com.payment.entity.dto.ProductItemDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    BaseResponse createOrder(OrderV0 order);

    BaseResponse getOrder(Long orderId);

    BaseResponse getOrderNo(String orderNo);

    BaseResponse addProduct(String orderNo, ProductItemDto productItem);

    BaseResponse deleteProduct(String orderNo, Long productId);

    BaseResponse payment(String orderNo);

    BaseResponse cancel(String orderNo, OrderCanselDto dto);

    BaseResponse submit(String orderNo);

    BaseResponse complete(String orderNo);

    BaseResponse getAllOrder();

    void deleteOutboxRecord();

    BaseResponse getPageable(Pageable pageable);

    BaseResponse getAllLoad(DataLoad load);
}
