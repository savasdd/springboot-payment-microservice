package com.payment.stock.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDetailV0 implements Serializable {

    private Long id;
    @Schema(example = "1")
    private Integer quantity;
    @Schema(example = "Adet")
    private UnitType unitType;
    @Schema(example = "TR")
    private String language;

}
