package com.payment.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class CompleteV0 implements Serializable {
    @Schema(example = "1111")
    private String orderNo;
}
