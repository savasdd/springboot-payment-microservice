package com.payment.entity.dto;

import com.payment.common.enums.OrderStatus;
import com.payment.entity.base.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto extends BaseDto implements Serializable {

    private String orderNo;
    @Schema(example = "3")
    private Long userId;
    private String paymentId;
    private OrderStatus orderStatus;
}
