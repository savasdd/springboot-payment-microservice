package com.payment.notification.common.event;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEvent implements Serializable {
    private String aggregateId;
}