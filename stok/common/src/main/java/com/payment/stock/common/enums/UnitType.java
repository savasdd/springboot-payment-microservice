package com.payment.stock.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UnitType {
    Adet("Adet"), Kilogram("Kilogram"), Gram("Gram"), Ton("Ton"), Litre("Litre"), Metre("Metre"), Santimetre("Santimetre");

    private final String type;

    UnitType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getContactType() {
        return type;
    }

    @JsonCreator
    public static UnitType fromValue(String value) {
        for (UnitType contact : values()) {
            String currentContact = contact.getContactType();
            if (currentContact.equals(value)) {
                return contact;
            }
        }

        throw new IllegalArgumentException("Invalid value for UnitType Enum: " + value);
    }
}
