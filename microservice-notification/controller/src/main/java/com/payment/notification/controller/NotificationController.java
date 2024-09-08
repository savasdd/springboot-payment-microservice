package com.payment.notification.controller;

import com.payment.notification.common.base.BaseResponse;
import com.payment.notification.entity.dto.NotificationDto;
import com.payment.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/payment/notification")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping(value = "/send")
    public ResponseEntity<BaseResponse> sendNotification(@RequestBody NotificationDto notification) {
        return ResponseEntity.ok(notificationService.sendNotification(notification));
    }
}
