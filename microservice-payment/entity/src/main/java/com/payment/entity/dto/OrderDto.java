package com.payment.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto implements Serializable {
    private String id;
    private Long userId;
    private List<ProductItemDto> productItems;
}
