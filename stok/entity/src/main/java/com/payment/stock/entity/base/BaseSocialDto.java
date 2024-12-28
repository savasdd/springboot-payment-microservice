package com.payment.stock.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseSocialDto extends BaseDto implements Serializable {

    private int likeCount;
    private int commentCount;
    private boolean liked;
    private boolean commented;

}