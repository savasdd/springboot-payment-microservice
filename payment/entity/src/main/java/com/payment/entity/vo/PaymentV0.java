package com.payment.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

@Data
public class PaymentV0 implements Serializable {

    @Schema(example = "1111")
    private String orderNo;
    @Schema(example = "SV22 705S 8188 660L 82Q3 1922")
    private String cartNo;
    @Schema(example = "09")
    private Integer cartExpMonth;
    @Schema(example = "2029")
    private Integer cartExpYear;
}
