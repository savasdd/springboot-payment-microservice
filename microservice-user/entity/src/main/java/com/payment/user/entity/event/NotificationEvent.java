package com.payment.user.entity.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class NotificationEvent extends BaseEvent implements Serializable {
    public static final String EVENT = "NOTIFICATION";
    private String id;
    private String userId;
    private String message;

    public NotificationEvent(String id, String userId, String message) {
        super(id);
        this.id = id;
        this.userId = userId;
        this.message = message;
    }
}