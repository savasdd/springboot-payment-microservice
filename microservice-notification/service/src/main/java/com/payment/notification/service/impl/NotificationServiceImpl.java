package com.payment.notification.service.impl;

import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payment.notification.common.base.BaseResponse;
import com.payment.notification.common.dto.NotificationDto;
import com.payment.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationServiceImpl implements NotificationService {

    @Override
    public BaseResponse sendNotification(NotificationDto dto) {

        Message message = getMessage(dto);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String gsonJson = gson.toJson(message);
        String response = send(message);

        log.info("Sending notification message: {} token: {} response: {}", gsonJson, dto.getToken(), response);
        return BaseResponse.builder().data("Send Success").build();
    }

    private String send(Message message) {
        try {
            return FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (Exception e) {
            log.error("Error while sending notification", e);
            return null;
        }
    }

    private Message getMessage(NotificationDto dto) {
        AndroidConfig androidConfig = getAndroidConfig(dto.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(dto.getTopic());
        Notification notification = Notification.builder().setTitle(dto.getTitle()).setBody(dto.getBody()).build();

        return Message.builder().setNotification(notification).setAndroidConfig(androidConfig).setApnsConfig(apnsConfig).setToken(dto.getToken()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder().setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder().setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic).setPriority(AndroidConfig.Priority.HIGH).setNotification(AndroidNotification.builder().setTag(topic).build()).build();
    }
}
