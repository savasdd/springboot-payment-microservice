package com.payment.stock.entity.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImageInfoDto implements Serializable {

    private Long stockId;
    private String name;
}
