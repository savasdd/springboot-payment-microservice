package com.payment.entity.vo;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderV0 implements Serializable {

    private List<ProductItemV0> items;
}
