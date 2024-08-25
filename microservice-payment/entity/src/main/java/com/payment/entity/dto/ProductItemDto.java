package com.payment.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemDto implements Serializable {
    private Long stockId;
    private BigDecimal price;
    private Integer quantity;
}
