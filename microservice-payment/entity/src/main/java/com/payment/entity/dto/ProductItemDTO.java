package com.payment.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductItemDTO {
    private UUID productId;
    private String productName;
    private BigDecimal productPrice;
    private Long productQuantity;
}
