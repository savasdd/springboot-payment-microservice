package com.payment.user.common.enums;

import lombok.Getter;

@Getter
public enum ElasticIndex {
    USER(1, "UserIndex");

    private final Integer code;
    private final String name;

    ElasticIndex(int i, String userIndex) {
        this.code = i;
        this.name = userIndex;
    }

}
