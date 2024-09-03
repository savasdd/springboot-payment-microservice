package com.payment.notification.common.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto implements Serializable {

    private String title;
    private String body;
    private String topic;
    private String token;
}
