package com.payment.gateway.common.filter;

import org.springframework.stereotype.Component;

@Component
public class NotifyFilter extends FilterFactory {

    public NotifyFilter() {
        super("NOTIFICATION");
    }
}