package com.payment.entity.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPayDto implements Serializable {
    @NotBlank
    @Size(min = 6, max = 250)
    private String paymentId;
}
