package com.payment.stock.common.enums;

import lombok.Getter;

@Getter
public enum ActionType {
    LIKE(0, "LIKE"),
    EMOTION(1, "EMOTION");

    private final Integer code;
    private final String name;

    ActionType(int i, String userIndex) {
        this.code = i;
        this.name = userIndex;
    }

}