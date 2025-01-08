package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.BasketType;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "BASKET")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Basket extends BaseEntity implements Serializable {

    @Column(name = "userId")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BasketType type;

    @Column(name = "quantity")
    private Integer quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "price")
    private BigDecimal price;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "disprice")
    private BigDecimal disprice;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "rate")
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "STOCK_ID", referencedColumnName = "ID")
    @ToString.Exclude
    private Stock stock;
}
