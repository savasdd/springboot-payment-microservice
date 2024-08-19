package com.payment.service.impl;

import com.payment.common.base.BaseResponse;
import com.payment.common.enums.OrderStatus;
import com.payment.common.utils.BeanUtil;
import com.payment.entity.dto.OrderDTO;
import com.payment.entity.dto.ProductItemDTO;
import com.payment.entity.model.Order;
import com.payment.entity.model.OutboxOrder;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductItemRepository itemRepository;
    private final OutboxOrderRepository outboxRepository;
    private final Publisher publisher;
    private final OutboxSerializer outboxSerializer;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse saveOrder(OrderDTO dto) {
        Order model = orderRepository.save(beanUtil.mapDto(dto, Order.class));

        if (!dto.getProductItems().isEmpty()) {
            List<ProductItem> itemList = beanUtil.mapAll(dto.getProductItems(), ProductItem.class);
            itemList.forEach(item -> item.setOrder(model));
            itemRepository.saveAll(itemList);
        }

        OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.createEvent(model));
        publishOutbox(outboxOrder);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse getOrder(String orderId) {
        return BaseResponse.builder().data(findOrder(orderId)).build();
    }

    @Override
    public BaseResponse addProduct(String orderId, ProductItemDTO dto) {
        Order order = findOrder(orderId);
        ProductItem productItem = beanUtil.mapDto(dto, ProductItem.class);
        productItem.setOrder(order);

        ProductItem model = itemRepository.save(productItem);
        OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.productAddedEvent(order, model));
        publishOutbox(outboxOrder);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse deleteProduct(String orderId, String productId) {
        if (itemRepository.existsById(productId)) {
            Order order = findOrder(orderId);
            itemRepository.deleteById(productId);

            OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.productRemovedEvent(order, productId));
            publishOutbox(outboxOrder);
        }
        return BaseResponse.builder().data("Success: " + productId).build();
    }

    @Override
    public BaseResponse payment(String orderId, String paymentId) {
        Order order = findOrder(orderId);
        order.setPaymentId(paymentId);
        order.setOrderStatus(OrderStatus.PAID);
        Order model = orderRepository.save(order);

        OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.paidEvent(model, paymentId));
        publishOutbox(outboxOrder);

        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse cancel(String orderId, String description) {
        Order order = findOrder(orderId);
        if (order.getOrderStatus().equals(OrderStatus.COMPLETED) || order.getOrderStatus().equals(OrderStatus.CANCELLED))
            throw new RuntimeException("cannot cansel order with id: " + orderId + " and status: " + order.getOrderStatus());

        order.setOrderStatus(OrderStatus.CANCELLED);
        Order model = orderRepository.save(order);
        OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.cancelledEvent(model, description));
        publishOutbox(outboxOrder);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse submit(String orderId) {
        Order order = findOrder(orderId);

        if (order.getOrderStatus().equals(OrderStatus.COMPLETED) || order.getOrderStatus().equals(OrderStatus.CANCELLED))
            throw new RuntimeException("cannot submit order with id: " + orderId + " and status: " + order.getOrderStatus());

        if (!order.getOrderStatus().equals(OrderStatus.PAID))
            throw new EntityNotFoundException("Order not paid");

        order.setOrderStatus(OrderStatus.SUBMITTED);
        Order model = orderRepository.save(order);
        OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.submittedEvent(model));
        publishOutbox(outboxOrder);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse complete(String orderId) {
        Order order = findOrder(orderId);

        if (order.getOrderStatus().equals(OrderStatus.CANCELLED) || !order.getOrderStatus().equals(OrderStatus.SUBMITTED))
            throw new RuntimeException("cannot complete order with id: " + orderId + " and status: " + order.getOrderStatus());

        order.setOrderStatus(OrderStatus.COMPLETED);
        Order model = orderRepository.save(order);
        OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.completedEvent(model));
        publishOutbox(outboxOrder);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse getAllOrder(Pageable pageable) {
        List<Order> orders = orderRepository.findAll(pageable).getContent();
        log.info("orders: {}", orders);
        return BaseResponse.builder().data(orders).count(orders.size()).build();
    }

    @Override
    public BaseResponse deleteOutboxRecord() {
        return null;
    }

    private void publishOutbox(OutboxOrder event) {
        try {
            log.info("publishing outbox event: {}", event);
            outboxRepository.deleteById(event.getId());
            publisher.publish(event.getEventType(), event.getAggregateId(), event); //TODO topicName

            log.info("outbox event published and deleted: {}", event);
        } catch (Exception e) {
            log.error("exception while publishing outbox event: {}", e.getLocalizedMessage());
        }
    }

    private Order findOrder(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

}
