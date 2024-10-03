package com.payment.notification.common.base;

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

    public static BaseResponse success(Object data) {
        return BaseResponse.builder().data(data).build();
    }

    public static BaseResponse success(Object data, Integer count) {
        return BaseResponse.builder().data(data).count(count).build();
    }

    public static BaseResponse error(String message) {
        return BaseResponse.builder().data(message).build();
    }
}