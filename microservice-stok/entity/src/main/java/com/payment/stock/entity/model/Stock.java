package com.payment.stock.entity.model;

import com.payment.stock.common.enums.PaymentType;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STOCK")
@EqualsAndHashCode(callSuper = true)
public class Stock extends BaseEntity implements Serializable {

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "stockName")
    private String stockName;

    @Column(name = "availableQuantity")
    private Integer availableQuantity;

    @Column(name = "reservedQuantity")
    private Integer reservedQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "unitType", nullable = false)
    private UnitType unitType;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentType", nullable = false)
    private PaymentType paymentType;

    @Column(name = "transactionDate")
    private Date transactionDate;
}
