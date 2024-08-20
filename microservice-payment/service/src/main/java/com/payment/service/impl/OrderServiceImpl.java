package com.payment.service.impl;

import com.payment.common.base.BaseResponse;
import com.payment.common.config.KafkaTopicsConfig;
import com.payment.common.enums.OrderStatus;
import com.payment.common.utils.BeanUtil;
import com.payment.entity.dto.OrderCanselDto;
import com.payment.entity.dto.OrderDto;
import com.payment.entity.dto.ProductItemDto;
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

import javax.lang.model.type.UnknownTypeException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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
    private final KafkaTopicsConfig topicsConfig;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse createOrder(OrderDto dto) {
        Order model = orderRepository.save(beanUtil.mapDto(dto, Order.class));

        if (!dto.getProductItems().isEmpty()) {
            List<ProductItem> itemList = beanUtil.mapAll(dto.getProductItems(), ProductItem.class);
            itemList.forEach(item -> item.setOrder(model));
            itemRepository.saveAll(itemList);
        }

        OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.createEvent(model));
        publishOutbox(outboxOrder);
        log.info("create order success {}", model);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse getOrder(String orderId) {
        return BaseResponse.builder().data(findOrder(orderId)).build();
    }

    @Override
    public BaseResponse addProduct(String orderId, ProductItemDto dto) {
        Order order = findOrder(orderId);
        ProductItem productItem = beanUtil.mapDto(dto, ProductItem.class);
        productItem.setOrder(order);

        ProductItem model = itemRepository.save(productItem);
        OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.productAddedEvent(order, model));
        publishOutbox(outboxOrder);
        log.info("add product success {}", productItem);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse deleteProduct(String orderId, String productId) {
        if (itemRepository.existsById(productId)) {
            Order order = findOrder(orderId);
            itemRepository.deleteById(productId);

            OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.productRemovedEvent(order, productId));
            publishOutbox(outboxOrder);
            log.info("delete product success {}", productId);
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
        log.info("payment success {}", paymentId);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse cancel(String orderId, OrderCanselDto dto) {
        Order order = findOrder(orderId);
        if (order.getOrderStatus().equals(OrderStatus.COMPLETED) || order.getOrderStatus().equals(OrderStatus.CANCELLED))
            throw new RuntimeException("cannot cansel order with id: " + orderId + " and status: " + order.getOrderStatus());

        order.setOrderStatus(OrderStatus.CANCELLED);
        Order model = orderRepository.save(order);
        OutboxOrder outboxOrder = outboxRepository.save(outboxSerializer.cancelledEvent(model, dto.getDescription()));
        publishOutbox(outboxOrder);

        log.info("cancel success {}", dto.getDescription());
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

        log.info("submit success {}", orderId);
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

        log.info("complete success {}", orderId);
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

    @Transactional
    public void publishOutbox(OutboxOrder event) {
        try {
            log.info("publishing outbox event: {}", event);
            //outboxRepository.deleteById(event.getId());
            publisher.publish(getTopicName(event.getEventType()), event.getAggregateId(), event);

            log.info("outbox event published and deleted: {}", event.getId());
        } catch (Exception e) {
            log.error("exception while publishing outbox event: {}", e.getLocalizedMessage());
        }
    }

    private Order findOrder(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    private String getTopicName(String eventType) {
        return switch (eventType) {
            case "ORDER_CANCELLED_EVENT" -> topicsConfig.getCancelled().getName();
            case "ORDER_COMPLETED" -> topicsConfig.getCompleted().getName();
            case "ORDER_CREATED" -> topicsConfig.getCreated().getName();
            case "ORDER_PAID" -> topicsConfig.getPayment().getName();
            case "PRODUCT_ITEM_ADDED" -> topicsConfig.getAdded().getName();
            case "PRODUCT_ITEM_REMOVED" -> topicsConfig.getRemoved().getName();
            case "ORDER_SUBMITTED" -> topicsConfig.getSubmitted().getName();
            default -> throw new EntityNotFoundException("Type not found");
        };
    }

}
