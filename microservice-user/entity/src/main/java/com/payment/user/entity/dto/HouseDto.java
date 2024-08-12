package com.payment.user.entity.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseDto implements Serializable {

    private Long id;
    private String street;
    private String language;
}
