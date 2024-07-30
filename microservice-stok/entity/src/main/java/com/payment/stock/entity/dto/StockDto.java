package com.payment.stock.entity.dto;

import com.payment.stock.common.enums.PaymentType;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.base.BaseDto;
import com.payment.stock.entity.base.BaseEntity;
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

    private Long userId;
    private String stockName;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private UnitType unitType;
    private PaymentType paymentType;
    private Date transactionDate;
}
