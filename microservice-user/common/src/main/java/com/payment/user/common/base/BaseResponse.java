package com.payment.user.common.base;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class BaseResponse implements Serializable {

    private Object data;
    private Integer count;

}
