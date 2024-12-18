package com.payment.service.impl;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.common.base.BaseResponse;
import com.payment.common.config.KafkaTopicsConfig;
import com.payment.common.config.UrlPropsConfig;
import com.payment.common.enums.OrderStatus;
import com.payment.common.utils.BeanUtil;
import com.payment.common.utils.ConstantUtil;
import com.payment.common.utils.RestUtil;
import com.payment.entity.dto.*;
import com.payment.entity.model.Order;
import com.payment.entity.model.ProductItem;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final OutboxSerializer outboxSerializer;
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
        this.outboxSerializer = outboxSerializer;
        this.notifySerializer = notifySerializer1;
        this.topicsConfig = topicsConfig1;
        this.propsConfig = propsConfig;
        this.beanUtil = beanUtil;
        this.restUtil = restUtil;
    }

    @Transactional
    @Override
    public BaseResponse createOrder(OrderV0 dto) {
        List<ProductItem> itemNewList = new ArrayList<>();
        List<ProductItem> itemList = new ArrayList<>();
        Order model;

        dto.getProductItems().forEach(item -> {
            StockDto stock = restUtil.exchangeGet(getUrlParam() + "findOne/" + item.getStockId(), StockDto.class);
            ProductItem product = beanUtil.mapDto(item, ProductItem.class);
            if (stock != null && item.getQuantity() <= stock.getAvailableQuantity()) {
                itemNewList.add(product);
                updateStockRest(item.getStockId(), item.getQuantity());
            } else {
                product.setStockName(stock != null ? stock.getStockName() : "");
                itemList.add(product);
            }
        });

        if (!itemNewList.isEmpty()) {
            model = orderRepository.save(beanUtil.mapDto(dto, Order.class));
            model.setOrderNo(generateOrderNo());
            itemNewList.forEach(item -> {
                item.setOrder(model);
                item.setOrderNo(model.getOrderNo());
            });
            itemRepository.saveAll(itemNewList);

            log.info("create order success {}", model);
            publishOutbox(outboxSerializer.createEvent(model));
            sendNotification(model.getUserId(), ConstantUtil.ORDER_SUCCESS + " - " + model.getOrderNo());
            return BaseResponse.success(beanUtil.mapDto(model, OrderDto.class));
        }


        return !itemList.isEmpty() ? BaseResponse.success("Stock Not Found: " + getStockName(itemList)) : BaseResponse.error("Order Exception");
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
    public BaseResponse addProduct(String orderNo, ProductItemDto dto) {
        Order order = findOrderNo(orderNo);
        ProductItem productItem = beanUtil.mapDto(dto, ProductItem.class);
        productItem.setOrder(order);
        productItem.setOrderNo(order.getOrderNo());
        ProductItem model = itemRepository.save(productItem);

        log.info("add product success {}", productItem);
        publishOutbox(outboxSerializer.productAddedEvent(order, model));
        sendNotification(order.getUserId(), ConstantUtil.PRODUCT_ADD + " - " + order.getOrderNo());
        return BaseResponse.success(model);
    }

    @Override
    public BaseResponse deleteProduct(String orderNo, Long productId) {
        if (itemRepository.existsById(productId)) {
            Order order = findOrderNo(orderNo);
            itemRepository.deleteById(productId);

            log.info("delete product success {}", productId);
            publishOutbox(outboxSerializer.productRemovedEvent(order, productId));
            sendNotification(order.getUserId(), ConstantUtil.PRODUCT_REMOVE + " - " + order.getOrderNo());
        }
        return BaseResponse.success("Delete product success");
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
        publishOutbox(outboxSerializer.paidEvent(model, paymentId));
        sendNotification(order.getUserId(), ConstantUtil.ORDER_PAYMENT + " - " + order.getOrderNo());
        return BaseResponse.success(model);
    }

    @Override
    public BaseResponse cancel(String orderNo, OrderCanselDto dto) {
        Order order = findOrderNo(orderNo);
        if (order.getOrderStatus().equals(OrderStatus.COMPLETED) || order.getOrderStatus().equals(OrderStatus.CANCELLED))
            throw new RuntimeException("cannot cansel order with id: " + orderNo + " and status: " + order.getOrderStatus());

        order.setOrderStatus(OrderStatus.CANCELLED);
        Order model = orderRepository.save(order);

        log.info("cancel success {}", dto.getDescription());
        publishOutbox(outboxSerializer.cancelledEvent(model, dto.getDescription()));
        sendNotification(order.getUserId(), ConstantUtil.ORDER_CANSEL + " - " + order.getOrderNo());
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
        publishOutbox(outboxSerializer.submittedEvent(model));
        sendNotification(order.getUserId(), ConstantUtil.ORDER_SUBMIT + " - " + order.getOrderNo());
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
        publishOutbox(outboxSerializer.completedEvent(model));
        sendNotification(order.getUserId(), ConstantUtil.ORDER_COMPETE + " - " + order.getOrderNo());
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
        return BaseResponse.success(beanUtil.mapAll(orders,OrderDto.class), orders.getTotalElements());
    }

    @Override
    public BaseResponse getAllLoad(DataLoad load) {
        BaseLoadResponse response = orderRepository.load(load);
        List<OrderDto> orderDtoList = beanUtil.mapAll(response.getData(), Order.class, OrderDto.class);
        log.info("Load orders: {}", response.getTotalCount());
        return BaseResponse.success(orderDtoList, response.getTotalCount());
    }

    @Transactional
    @Override
    public void deleteOutboxRecord() {
        outboxRepository.deleteOutboxRecordByLimit();
    }


    public void updateStockRest(Long id, Integer quantity) {
        try {
            Integer stock = restUtil.exchangeGet(getUrlParam() + "update-quantity/" + id, quantity);
            log.info("update stock: {}", stock);
        } catch (Exception e) {
            log.error("exception while update stock: {}", e.getLocalizedMessage());
        }
    }


    private Order findOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }


    private String getStockName(List<ProductItem> itemDtoList) {
        return itemDtoList.stream().map(ProductItem::getStockName).collect(Collectors.joining(",", "[", "]"));
    }

    private String getUrlParam() {
        return parameterRepository.findByKey(propsConfig.getStock()).orElseThrow(() -> new EntityNotFoundException("Not Found")).getValue();
    }


}
