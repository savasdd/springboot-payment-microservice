package com.payment.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.payment.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@Table(name = "PRODUCT_ITEM")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductItem extends BaseEntity implements Serializable {

    @Column(name = "stockId", nullable = false)
    private Long stockId;

    @Column(name = "stockName", nullable = false)
    private String stockName;

    @Column(name = "price")
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "quantity")
    private Integer quantity = 0;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    private Order order;

    public ProductItem(Long stockId, String stockName, BigDecimal price, Integer quantity, Order order) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.price = price;
        this.quantity = quantity;
        this.order = order;
    }
}
