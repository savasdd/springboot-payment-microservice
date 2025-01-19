package com.payment.service.impl;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.common.base.BaseResponse;
import com.payment.common.config.KafkaTopicsConfig;
import com.payment.common.config.UrlPropsConfig;
import com.payment.common.enums.EventType;
import com.payment.common.enums.OrderStatus;
import com.payment.common.enums.RecordStatus;
import com.payment.common.utils.BeanUtil;
import com.payment.common.utils.RestUtil;
import com.payment.entity.dto.*;
import com.payment.entity.model.Order;
import com.payment.entity.model.ProductItem;
import com.payment.entity.vo.ItemV0;
import com.payment.entity.vo.OrderV0;
import com.payment.entity.vo.ProductItemV0;
import com.payment.repository.OrderRepository;
import com.payment.repository.OutboxOrderRepository;
import com.payment.repository.ParameterRepository;
import com.payment.repository.ProductItemRepository;
import com.payment.service.OrderService;
import com.payment.service.base.BaseService;
import com.payment.service.publisher.NotifySerializer;
import com.payment.service.publisher.OutboxSerializer;
import com.payment.service.publisher.Publisher;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@EqualsAndHashCode(callSuper = true)
public class OrderServiceImpl extends BaseService implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductItemRepository itemRepository;
    private final OutboxOrderRepository outboxRepository;
    private final ParameterRepository parameterRepository;
    private final Publisher publisher;
    private final OutboxSerializer outbox;
    private final NotifySerializer notifySerializer;
    private final KafkaTopicsConfig topicsConfig;
    private final UrlPropsConfig propsConfig;
    private final BeanUtil beanUtil;
    private final RestUtil restUtil;

    public OrderServiceImpl(OutboxOrderRepository outboxRepository, Publisher publisher, NotifySerializer notifySerializer, KafkaTopicsConfig topicsConfig, OrderRepository orderRepository, ProductItemRepository itemRepository, OutboxOrderRepository outboxRepository1, ParameterRepository parameterRepository, Publisher publisher1, OutboxSerializer outboxSerializer, NotifySerializer notifySerializer1, KafkaTopicsConfig topicsConfig1, UrlPropsConfig propsConfig, BeanUtil beanUtil, RestUtil restUtil) {
        super(outboxRepository, publisher, notifySerializer, topicsConfig);
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.outboxRepository = outboxRepository1;
        this.parameterRepository = parameterRepository;
        this.publisher = publisher1;
        this.outbox = outboxSerializer;
        this.notifySerializer = notifySerializer1;
        this.topicsConfig = topicsConfig1;
        this.propsConfig = propsConfig;
        this.beanUtil = beanUtil;
        this.restUtil = restUtil;
    }

    @Override
    public BaseResponse createOrder(Long userId, OrderV0 dto) {
        Order order = beanUtil.mapDto(dto, Order.class);
        order.setUserId(userId);
        order.setOrderNo(generateOrderNo());
        order.getItems().forEach(d -> d.setOrder(order));
        Order model = orderRepository.save(order);

        log.info("create order {}", model);
        publishOutboxNotification(outbox.event(model, EventType.CREATED), order);
        return BaseResponse.success(beanUtil.mapDto(model, OrderDto.class));
    }

    @Override
    public BaseResponse getOrder(Long orderId) {
        return BaseResponse.success(orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Entity not found")));
    }

    @Override
    public BaseResponse getOrderNo(String orderNo) {
        return BaseResponse.success(findOrderNo(orderNo));
    }

    @Override
    public BaseResponse addItem(String orderNo, ProductItemV0 dto) {
        Order order = findOrderNo(orderNo);
        ProductItem item = beanUtil.mapDto(dto, ProductItem.class);
        setItems(order, item);
        Order model = orderRepository.save(order);

        log.info("add item {}", dto);
        publishOutboxNotification(outbox.event(model, EventType.ADDED), order);
        return BaseResponse.success(model);
    }


    @Override
    public BaseResponse removeItem(String orderNo, ItemV0 vo) {
        Order order = findOrderNo(orderNo);
        List<ProductItem> items = order.getItems().stream().filter(f -> f.getId().equals(vo.getId())).toList();
        ProductItem item = items.isEmpty() ? null : items.get(0);
        order.getItems().stream().filter(f -> f.getId().equals(vo.getId())).toList().stream().peek(p -> p.setRecordStatus(RecordStatus.DELETED)).toList();

        Order model = orderRepository.save(order);
        log.info("remove item {}", item);
        publishOutboxNotification(outbox.event(model, EventType.REMOVED), order);
        return BaseResponse.success(model);
    }

    @Override
    public BaseResponse payment(String orderNo) {
        String paymentId = generatePaymentNo();
        Order order = findOrderNo(orderNo);

        if (order.getOrderStatus().equals(OrderStatus.CANCELLED))
            throw new RuntimeException("cannot payment order with id: " + orderNo + " and status: " + order.getOrderStatus());

        order.setPaymentId(paymentId);
        order.setOrderStatus(OrderStatus.PAID);
        Order model = orderRepository.save(order);

        log.info("payment success {}", paymentId);
        publishOutboxNotification(outbox.event(model, EventType.PAID), order);
        return BaseResponse.success(model);
    }

    @Override
    public BaseResponse cancel(String orderNo, OrderCanselDto dto) {
        Order order = findOrderNo(orderNo);
        if (order.getOrderStatus().equals(OrderStatus.COMPLETED) || order.getOrderStatus().equals(OrderStatus.CANCELLED))
            throw new RuntimeException("cannot cansel order with id: " + orderNo + " and status: " + order.getOrderStatus());

        if (Objects.isNull(dto.getDescription()))
            return BaseResponse.error("Description can't be empty");

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setDescription(dto.getDescription());
        Order model = orderRepository.save(order);

        log.info("cancel success {}", dto.getDescription());
        publishOutboxNotification(outbox.event(model, EventType.CANCELLED), order);
        return BaseResponse.success(model);
    }

    @Override
    public BaseResponse submit(String orderNo) {
        Order order = findOrderNo(orderNo);

        if (order.getOrderStatus().equals(OrderStatus.COMPLETED) || order.getOrderStatus().equals(OrderStatus.CANCELLED))
            throw new RuntimeException("cannot submit order with id: " + orderNo + " and status: " + order.getOrderStatus());

        if (!order.getOrderStatus().equals(OrderStatus.PAID))
            throw new EntityNotFoundException("Order not paid");

        order.setOrderStatus(OrderStatus.SUBMITTED);
        Order model = orderRepository.save(order);

        log.info("submit success {}", orderNo);
        publishOutboxNotification(outbox.event(model, EventType.SUBMITTED), order);
        return BaseResponse.success(model);
    }

    @Override
    public BaseResponse complete(String orderNo) {
        Order order = findOrderNo(orderNo);

        if (order.getOrderStatus().equals(OrderStatus.CANCELLED) || !order.getOrderStatus().equals(OrderStatus.SUBMITTED))
            throw new RuntimeException("cannot complete order with id: " + orderNo + " and status: " + order.getOrderStatus());

        order.setOrderStatus(OrderStatus.COMPLETED);
        Order model = orderRepository.save(order);

        log.info("complete success {}", orderNo);
        publishOutboxNotification(outbox.event(model, EventType.COMPLETED), order);
        return BaseResponse.success(model);
    }

    @Override
    public BaseResponse getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        log.info("all orders: {}", orders.size());
        return BaseResponse.success(orders, (long) orders.size());
    }

    @Override
    public BaseResponse getPageable(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        log.info("pageable orders: {}", orders.getTotalElements());
        return BaseResponse.success(beanUtil.mapAll(orders, OrderDto.class), orders.getTotalElements());
    }

    @Override
    public BaseResponse getAllLoad(DataLoad load) {
        BaseLoadResponse response = orderRepository.load(load);
        List<OrderDto> orderDtoList = beanUtil.mapAll(response.getData(), Order.class, OrderDto.class);
        log.info("Load orders: {}", response.getTotalCount());
        return BaseResponse.success(orderDtoList, response.getTotalCount());
    }

    @Override
    @Transactional
    public void deleteOutboxRecord() {
        outboxRepository.deleteOutboxRecordByLimit();
    }

    private Order findOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    private static void setItems(Order order, ProductItem item) {
        List<ProductItem> items = new ArrayList<>();
        item.setOrder(order);
        order.getItems().add(item);
        order.getItems().forEach(f -> {
            ProductItem newItem = new ProductItem(f.getStockId(), f.getStockName(), f.getPrice(), f.getQuantity(), order);
            BeanUtils.copyProperties(f, newItem);
            items.add(newItem);
        });
        order.setItems(items);
    }


    public void updateStockRest(Long id, Integer quantity) {
        try {
            Integer stock = restUtil.exchangeGet(getUrlParam() + "update-quantity/" + id, quantity);
            //StockDto stock = restUtil.exchangeGet(getUrlParam() + "findOne/" + item.getStockId(), StockDto.class);
            log.info("update stock: {}", stock);
        } catch (Exception e) {
            log.error("exception while update stock: {}", e.getLocalizedMessage());
        }
    }

    private String getStockName(List<ProductItem> itemDtoList) {
        return itemDtoList.stream().map(ProductItem::getStockName).collect(Collectors.joining(",", "[", "]"));
    }

    private String getUrlParam() {
        return parameterRepository.findByKey(propsConfig.getStock()).orElseThrow(() -> new EntityNotFoundException("Not Found")).getValue();
    }


}
