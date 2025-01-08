package com.payment.stock.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.BasketType;
import com.payment.stock.entity.base.BaseDto;
import com.payment.stock.entity.model.Stock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasketDto extends BaseDto implements Serializable {

    private Long userId;
    private BasketType type;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal disprice;
    private BigDecimal rate;
    private StockDto stock;
}
