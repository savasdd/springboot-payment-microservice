package com.payment.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Data
public class CanselV0 implements Serializable {
    @Schema(example = "1111")
    private String orderNo;
    @Schema(example = "Ürünü almaktan vazgeçtim. Teşekkürler.")
    private String description;
}
