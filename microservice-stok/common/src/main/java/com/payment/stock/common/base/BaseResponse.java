package com.payment.stock.common.base;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseResponse implements Serializable {

    private Object data;
    private Integer count;

}
