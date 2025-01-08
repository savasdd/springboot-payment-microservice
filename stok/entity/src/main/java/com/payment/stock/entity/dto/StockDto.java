package com.payment.stock.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.entity.base.BaseDto;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDto extends BaseDto implements Serializable {

    private Long userId;
    private String stockName;
    private BigDecimal price;
    private StockRateDto rate;
    private List<CategoryDto> categoryList;
    private List<PropertyDto> propertyList;
    private List<StockDetailDto> details;
}
