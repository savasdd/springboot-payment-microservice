package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "STOCK_RATE")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockRate extends BaseEntity implements Serializable {

    @Column(name = "rateName", nullable = false)
    private String rateName;

    @Column(name = "rate")
    private BigDecimal rate;
}
