package com.payment.gateway.common.base;

import lombok.*;
import org.springframework.http.HttpStatus;

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
    private Integer status;

    public static BaseResponse success(Object data) {
        return BaseResponse.builder().data(data).status(HttpStatus.OK.value()).build();
    }

    public static BaseResponse success(Object data, Integer count) {
        return BaseResponse.builder().data(data).status(HttpStatus.OK.value()).count(count).build();
    }

    public static BaseResponse error(String message) {
        return BaseResponse.builder().data(message).status(HttpStatus.BAD_REQUEST.value()).build();
    }

}