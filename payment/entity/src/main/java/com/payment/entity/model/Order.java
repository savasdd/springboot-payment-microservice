package com.payment.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.payment.common.enums.OrderStatus;
import com.payment.entity.base.BaseEntity;
import lombok.*;
import org.apache.kafka.common.protocol.types.Field;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "ORDERS")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order extends BaseEntity implements Serializable {

    @Column(name = "orderNo")
    private String orderNo;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "paymentNo")
    private String paymentNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus")
    private OrderStatus orderStatus = OrderStatus.NEW;

    @Column(name = "description", length = 2000)
    private String description;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductItem> items;

    @Column(name = "cartNo")
    private String cartNo;

    @Column(name = "cartExpMonth")
    private Integer cartExpMonth;

    @Column(name = "cartExpYear")
    private Integer cartExpYear;

    @Column(name = "securityCode")
    private String securityCode;
}
