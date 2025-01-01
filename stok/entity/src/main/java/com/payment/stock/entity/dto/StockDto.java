package com.payment.stock.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseDto;
import com.payment.stock.entity.vo.PropertyV0;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDto extends BaseDto implements Serializable {

    private Long userId;
    private String stockName;
    private Integer availableQuantity;
    private UnitType unitType;
    private BigDecimal price;
    private Integer year;
    private StockRateDto rate;
    private CategoryDto category;
    private List<PropertyDto> propertyList = new ArrayList<>();
    private List<StockDetailDto> details;
}
