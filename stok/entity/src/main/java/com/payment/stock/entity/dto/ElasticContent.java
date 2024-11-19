package com.payment.stock.entity.dto;

import com.payment.stock.common.enums.UnitType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElasticContent implements Serializable {
    private Long id;
    private String contentType;
    private Integer contentId;
    private Long userId;
    private String stockName;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private UnitType unitType;
}