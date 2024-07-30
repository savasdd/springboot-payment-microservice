package com.payment.stock.common.base;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class BaseResponse implements Serializable {

    private Object data;
    private Integer count = 0;

}
