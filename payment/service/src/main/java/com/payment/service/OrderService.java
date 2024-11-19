package com.payment.service;

import com.payment.common.base.BaseResponse;
import com.payment.entity.dto.OrderCanselDto;
import com.payment.entity.dto.OrderDto;
import com.payment.entity.dto.OrderV0;
import com.payment.entity.dto.ProductItemDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    BaseResponse createOrder(OrderV0 order);

    BaseResponse getOrder(String orderId);

    BaseResponse getOrderNo(String orderNo);

    BaseResponse addProduct(String orderNo, ProductItemDto productItem);

    BaseResponse deleteProduct(String orderNo, String productId);

    BaseResponse payment(String orderNo);

    BaseResponse cancel(String orderNo, OrderCanselDto dto);

    BaseResponse submit(String orderNo);

    BaseResponse complete(String orderNo);

    BaseResponse getAllOrder(Pageable pageable);

    void deleteOutboxRecord();

}
