package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "STOCK")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock extends BaseEntity implements Serializable {

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "stockName")
    private String stockName;

    @Column(name = "availableQuantity")
    private Integer availableQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "unitType", nullable = false)
    private UnitType unitType;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "RATE_ID", referencedColumnName = "ID")
    private StockRate rate;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stock", orphanRemoval = true)
    @JsonIgnoreProperties("stock")
    private List<StockDetail> details;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "STOCK_CATEGORY", joinColumns = @JoinColumn(name = "stock_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categoryList;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "STOCK_PROPERTY", joinColumns = @JoinColumn(name = "stock_id"), inverseJoinColumns = @JoinColumn(name = "property_id"))
    private List<Property> propertyList;
}
