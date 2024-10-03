package com.payment.notification.service.impl;

import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payment.notification.common.base.BaseResponse;
import com.payment.notification.common.enums.RecordStatus;
import com.payment.notification.common.utils.ConstantUtil;
import com.payment.notification.entity.dto.NotificationDto;
import com.payment.notification.entity.model.FirebaseLog;
import com.payment.notification.repository.FirebaseLogRepository;
import com.payment.notification.repository.FirebaseTokenRepository;
import com.payment.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationServiceImpl implements NotificationService {

    private final FirebaseTokenRepository repository;
    private final FirebaseLogRepository logRepository;

    @Override
    public BaseResponse sendNotification(NotificationDto dto) {
        try {
            dto.setTitle(dto.getTitle() != null ? dto.getTitle() : ConstantUtil.FCM_TITLE);
            dto.setTopic(ConstantUtil.FCM_TOPIC);
            dto.setToken(repository.findBySenderIdAndRecordStatus(ConstantUtil.FCM_SENDER_ID, RecordStatus.ACTIVE).orElseThrow(() -> new EntityNotFoundException("Token Not Found!")).getToken());

            Message message = getMessage(dto);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String gsonJson = gson.toJson(message);
            String response = send(message);

            log.info("Sending notification message: {} response: {}", gsonJson, response);
            createLog(dto);
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            throw new RuntimeException(e);

        }

        return BaseResponse.success("Send Success");
    }

    private String send(Message message) throws ExecutionException, InterruptedException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private Message getMessage(NotificationDto dto) {
        AndroidConfig androidConfig = getAndroidConfig(dto.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(dto.getTopic());
        WebpushConfig webpushConfig = getWebpushConfig(dto);
        Notification notification = Notification.builder().setTitle(dto.getTitle()).setBody(dto.getBody()).build();

        return Message.builder().setNotification(notification).setAndroidConfig(androidConfig).setApnsConfig(apnsConfig).setWebpushConfig(webpushConfig).setToken(dto.getToken()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder().setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder().setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic).setNotification(AndroidNotification.builder().setTag(topic).build()).build();
    }

    private WebpushConfig getWebpushConfig(NotificationDto dto) {
        return WebpushConfig.builder().setNotification(WebpushNotification.builder().setTag(dto.getTitle()).setBody(dto.getBody()).build()).putData("userId", dto.getUserId()).build();
    }

    private void createLog(NotificationDto dto) {
        FirebaseLog firebaseLog = FirebaseLog.builder().userId(dto.getUserId()).title(dto.getTitle()).body(dto.getBody()).topic(dto.getTopic()).token(dto.getToken()).build();
        logRepository.save(firebaseLog);
        log.info("createLog: {}", firebaseLog);
    }
}
