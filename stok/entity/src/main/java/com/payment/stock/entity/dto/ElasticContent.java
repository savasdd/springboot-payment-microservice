package com.payment.stock.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.UnitType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticContent implements Serializable {
    private Long id;
    private String contentType;
    private Integer contentId;

    private Long userId;
    private String stockName;
    private Integer availableQuantity;
    private UnitType unitType;
    private String rateName;
    private String percent;
    private String image;
}