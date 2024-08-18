package com.payment.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderDTO {
    private String id;
    private Long userId;
    private List<ProductItemDTO> productItems;
}
