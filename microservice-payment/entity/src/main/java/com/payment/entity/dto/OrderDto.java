package com.payment.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String orderNo;
    @Schema(example = "3")
    private Long userId;
    private List<ProductItemDto> productItems;
}
