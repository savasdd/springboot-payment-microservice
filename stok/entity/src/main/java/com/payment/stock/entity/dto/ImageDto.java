package com.payment.stock.entity.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ImageDto implements Serializable {

    private String name;
    private String link;
    private Long size;
}
