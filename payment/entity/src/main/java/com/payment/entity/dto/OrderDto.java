package com.payment.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.payment.common.enums.OrderStatus;
import com.payment.entity.base.BaseDto;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto extends BaseDto implements Serializable {

    private String orderNo;
    private Long userId;
    private String paymentNo;
    private OrderStatus orderStatus;
    private String description;
    private List<ProductItemDto> items;
}
