package com.payment.entity.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDto implements Serializable {

    private Long id;
    private String stockName;
    private Integer availableQuantity;
}
