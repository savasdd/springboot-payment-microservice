package com.payment.user.entity.vo;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenVo implements Serializable {
    private String token;
}
