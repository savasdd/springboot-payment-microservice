package com.payment.stock.entity.dto;

import com.payment.stock.entity.base.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDetailDto extends BaseDto implements Serializable {

    @Schema(example = "Test")
    private String title;

    @Schema(example = "Test Desc")
    private String description;

    @Schema(example = "TR")
    private String language;
}
