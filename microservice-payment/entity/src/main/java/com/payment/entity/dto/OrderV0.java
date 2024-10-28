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
public class OrderV0 implements Serializable {

    @Schema(example = "3")
    private Long userId;
    private List<ProductItemDto> productItems;
}
