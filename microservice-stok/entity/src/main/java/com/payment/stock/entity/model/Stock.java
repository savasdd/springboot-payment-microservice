package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "unitType", nullable = false)
    private UnitType unitType;

    @JsonIgnore
    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockDetail> details = new ArrayList<>();
}
