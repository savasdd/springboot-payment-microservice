package com.payment.notification.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto implements Serializable {

    @Schema(example = "Bildirim")
    private String title;
    @Schema(example = "Deneme")
    private String body;
    @Schema(example = "notification")
    private String topic;
    @Schema(example = "ffce688d-4e53-46b8-a32a-49cf7e15a18dffce688d-4e53-46b8-a32a-49cf7e15a18d")
    private String token;
}
