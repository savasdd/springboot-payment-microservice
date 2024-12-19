package com.payment.stock.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.entity.base.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto extends BaseDto implements Serializable {

    @Schema(example = "İnşaat")
    private String categoryName;
    @Schema(example = "İnşaat Malzemeleri")
    private String description;
}
