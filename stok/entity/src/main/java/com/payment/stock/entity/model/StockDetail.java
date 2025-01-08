package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "STOCK_DETAILS")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDetail extends BaseEntity implements Serializable {

    @Column(name = "language")
    private String language;

    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "unitType", nullable = false)
    private UnitType unitType;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "STOCK_ID", referencedColumnName = "ID")
    private Stock stock;

    public StockDetail(String language, Integer quantity, UnitType unitType, Stock stock) {
        this.language = language;
        this.quantity = quantity;
        this.unitType = unitType;
        this.stock = stock;
    }
}
