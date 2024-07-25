package com.payment.report.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BaseResponse {

    private Object data;
    private Integer count;
}
