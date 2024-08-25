package com.payment.notification.service.impl;

import com.payment.notification.common.event.NotificationEvent;
import com.payment.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNotification(NotificationEvent event) {
        log.info(event.getMessage());
    }
}
