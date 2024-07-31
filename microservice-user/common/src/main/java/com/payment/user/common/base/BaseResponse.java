package com.payment.user.common.base;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseResponse implements Serializable {

    private Object data;
    private Integer count;

}
