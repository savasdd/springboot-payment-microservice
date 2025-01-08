package com.payment.stock.entity.vo;

import com.payment.stock.common.enums.BasketType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class BasketV0 implements Serializable {

    @Schema(example = "NEW")
    private BasketType type;
    @Schema(example = "1")
    private Integer quantity;
    private StockInfoV0 stock;
}
