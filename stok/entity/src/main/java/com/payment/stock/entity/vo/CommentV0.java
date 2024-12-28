package com.payment.stock.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.entity.base.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentV0 extends BaseDto implements Serializable {

    @Schema(example = "Güzel ürün")
    private String comment;
    @Schema(example = "TR")
    private String language;
    private StockInfoV0 stock;
}
