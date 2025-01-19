package com.payment.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum EventType {
    CREATED("EVENT_CREATED", "Sipariş Başarılıyla Oluşturuldu"),
    CANCELLED("EVENT_CANCELLED", "Ödeme Başarıyla İptal Edilmiştir"),
    COMPLETED("EVENT_COMPLETED", "Ödeme Başarıyla Tamamlanmıştır"),
    PAID("EVENT_PAID", "Ödeme Başarıyla Alınmıştır"),
    ADDED("EVENT_ADDED", "Ürün Başarılıyla Eklendi"),
    REMOVED("EVENT_REMOVED", "Ürün Başarılıyla Silindi"),
    SUBMITTED("EVENT_SUBMITTED", "Ödeme Başarıyla Kabul Edilmiştir"),
    NOTIFICATION("EVENT_NOTIFICATION", "Bildirim İletildi");

    private final String name;
    private final String message;

    EventType(String name, String message) {
        this.name = name;
        this.message = message;
    }


    @JsonCreator
    public static EventType fromValue(String value) {
        for (EventType contact : values()) {
            String currentContact = contact.getName();
            if (currentContact.equals(value)) {
                return contact;
            }
        }

        throw new IllegalArgumentException("Invalid value for Type Enum: " + value);
    }
}