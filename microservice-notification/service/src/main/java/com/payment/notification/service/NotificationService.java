package com.payment.notification.service;

import com.payment.notification.common.base.BaseResponse;
import com.payment.notification.common.dto.NotificationDto;

public interface NotificationService {

    BaseResponse sendNotification(NotificationDto dto);
}
