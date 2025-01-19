package com.payment.entity.dto;

import com.payment.entity.base.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemDto extends BaseDto implements Serializable {
    private Long stockId;
    private Long basketId;
    private String stockName;
    private BigDecimal price;
    private Integer quantity;
}
