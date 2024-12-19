package com.payment.stock.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseDto;
import com.payment.stock.entity.dto.StockDetailDto;
import com.payment.stock.entity.dto.StockRateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockV0 extends BaseDto implements Serializable {

    @Schema(example = "22")
    private Long userId;
    @Schema(example = "Demir")
    private String stockName;
    @Schema(example = "1")
    private Integer availableQuantity;
    @Schema(example = "Adet")
    private UnitType unitType;
    @Schema(example = "10.5")
    private BigDecimal price;
    @Schema(example = "2024")
    private Integer year;
    private StockRateV0 rate;
    private CategoryV0 category;
    private List<StockDetailDto> details;
}
