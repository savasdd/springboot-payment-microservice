package com.payment.stock.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockRateDto extends BaseDto implements Serializable {

    @Schema(example = "Kasım İndirimi")
    private String rateName;
    @Schema(example = "2.25")
    private BigDecimal rate;
}
