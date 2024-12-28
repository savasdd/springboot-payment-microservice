package com.payment.stock.entity.dto;

import com.payment.stock.entity.base.BaseSocialDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommentDto extends BaseSocialDto implements Serializable {

    private String comment;
    private String language;
    private StockInfoDto stock;
}
