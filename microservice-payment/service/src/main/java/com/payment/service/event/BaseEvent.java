package com.payment.service.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class BaseEvent implements Serializable {
    private String aggregateId;
}
