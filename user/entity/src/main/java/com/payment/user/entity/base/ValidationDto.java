package com.payment.user.entity.base;

import com.payment.user.common.base.BaseResponse;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationDto implements Serializable {

    private boolean error;
    private String message;

    public static ValidationDto validation(boolean error,String message) {
        return ValidationDto.builder().error(error).message(message).build();
    }
}
