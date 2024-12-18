package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationEvent extends BaseEvent {
    public static final String EVENT = "NOTIFICATION";
    private Long id;
    private String userId;
    private String message;

    public NotificationEvent(Long id, String userId, String message) {
        super(id);
        this.id = id;
        this.userId = userId;
        this.message = message;
    }
}
