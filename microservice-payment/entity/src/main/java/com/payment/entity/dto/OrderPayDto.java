package com.payment.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class OrderPayDto {
    @NotBlank
    @Size(min = 6, max = 250)
    private String paymentId;
}
