package com.payment.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class OrderCanselDTO {
    @Size(min = 6, max = 1000)
    private String reason;
}
