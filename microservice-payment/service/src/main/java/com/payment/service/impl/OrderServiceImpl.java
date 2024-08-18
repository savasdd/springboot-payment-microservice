package com.payment.service.impl;

import com.payment.common.base.BaseResponse;
import com.payment.entity.model.Order;
import com.payment.entity.model.ProductItem;
import com.payment.repository.OrderRepository;
import com.payment.repository.OutboxOrderRepository;
import com.payment.repository.ProductItemRepository;
import com.payment.service.OrderService;
import com.payment.service.publisher.Publisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductItemRepository itemRepository;
    private final OutboxOrderRepository outboxOrderRepository;
    private final Publisher publisher;

    @Override
    public BaseResponse createOrder(Order order) {
        return null;
    }

    @Override
    public BaseResponse getOrder(String orderId) {
        return null;
    }

    @Override
    public BaseResponse addProduct(ProductItem productItem) {
        return null;
    }

    @Override
    public BaseResponse deleteProduct(String orderId, String productId) {
        return null;
    }

    @Override
    public BaseResponse pay(String orderId, String paymentId) {
        return null;
    }

    @Override
    public BaseResponse cancel(String orderId, String reason) {
        return null;
    }

    @Override
    public BaseResponse submit(String orderId) {
        return null;
    }

    @Override
    public BaseResponse complete(String orderId) {
        return null;
    }

    @Override
    public BaseResponse getAll(Pageable pageable) {
        return null;
    }

    @Override
    public BaseResponse deleteOutboxRecord() {
        return null;
    }
}
