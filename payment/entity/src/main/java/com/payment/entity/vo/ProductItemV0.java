package com.payment.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemV0 implements Serializable {
    private Long id;
    @Schema(example = "1")
    private Long stockId;
    @Schema(example = "test")
    private String stockName;
    @Schema(example = "10.5")
    private BigDecimal price;
    @Schema(example = "2")
    private Integer quantity;
}
