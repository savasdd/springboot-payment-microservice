package com.payment.stock.common.enums;

import lombok.Getter;

@Getter
public enum ElasticIndex {
    STOCK(1, "STOCK");

    private final Integer code;
    private final String name;

    ElasticIndex(int i, String userIndex) {
        this.code = i;
        this.name = userIndex;
    }

}