package com.payment.service;

import com.payment.common.base.BaseResponse;
import com.payment.entity.dto.OrderCanselDTO;
import com.payment.entity.dto.OrderDTO;
import com.payment.entity.dto.ProductItemDTO;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    BaseResponse createOrder(OrderDTO order);

    BaseResponse getOrder(String orderId);

    BaseResponse addProduct(String orderId, ProductItemDTO productItem);

    BaseResponse deleteProduct(String orderId, String productId);

    BaseResponse payment(String orderId, String paymentId);

    BaseResponse cancel(String orderId, OrderCanselDTO dto);

    BaseResponse submit(String orderId);

    BaseResponse complete(String orderId);

    BaseResponse getAllOrder(Pageable pageable);

    BaseResponse deleteOutboxRecord();

}
