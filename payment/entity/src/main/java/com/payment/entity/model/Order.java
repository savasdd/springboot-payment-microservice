package com.payment.entity.model;

import com.payment.common.enums.OrderStatus;
import com.payment.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORDERS")
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity implements Serializable {

    @Column(name = "orderNo")
    private String orderNo;
    @Column(name = "userId", nullable = false)
    private Long userId;
    @Column(name = "paymentId")
    private String paymentId;
    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus")
    private OrderStatus orderStatus = OrderStatus.NEW;


}
