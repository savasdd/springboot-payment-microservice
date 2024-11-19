package com.payment.user.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto implements Serializable {
    private String token;
    private long expiresIn;
    private List<String> roles = new ArrayList<>();
}
