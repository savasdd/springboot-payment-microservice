package com.payment.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCT_ITEM")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductItem extends BaseEntity implements Serializable {

    @Column(name = "stockId", nullable = false)
    private Long stockId;
    @Column(name = "price")
    private BigDecimal price = BigDecimal.ZERO;
    @Column(name = "quantity")
    private Integer quantity = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    private Order order;

    @Column(name = "orderNo")
    private String orderNo;

    @Transient
    private String stockName;
}
