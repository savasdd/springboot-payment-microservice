package com.payment.stock.entity.dto;

import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDto extends BaseDto implements Serializable {

    @Schema(example = "22")
    private Long userId;
    @Schema(example = "Demir")
    private String stockName;
    @Schema(example = "1")
    private Integer availableQuantity;
    @Schema(example = "Adet")
    private UnitType unitType;
}
