package com.payment.stock.entity.dto;

import com.payment.stock.common.enums.PaymentType;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseDto;
import com.payment.stock.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDto extends BaseDto implements Serializable {

    @Schema(example = "22")
    private Long userId;
    @Schema(example = "Demir")
    private String stockName;
    @Schema(example = "1")
    private Integer availableQuantity;
    @Schema(example = "1")
    private Integer reservedQuantity;
    private UnitType unitType;
    private PaymentType paymentType;
    private Date transactionDate;
}
