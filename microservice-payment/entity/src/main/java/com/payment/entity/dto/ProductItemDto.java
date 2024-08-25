package com.payment.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemDto implements Serializable {
    @Schema(example = "15")
    private Long stockId;
    @Schema(example = "10")
    private BigDecimal price;
    @Schema(example = "1")
    private Integer quantity;
}
