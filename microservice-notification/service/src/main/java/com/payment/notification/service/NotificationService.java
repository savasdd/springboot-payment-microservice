package com.payment.notification.service;

import com.payment.notification.common.event.NotificationEvent;

public interface NotificationService {
    void sendNotification(NotificationEvent event);
}
