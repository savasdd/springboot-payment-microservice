package com.payment.notification.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Schema(example = "22")
    private String userId;
    @Schema(example = "Bildirim")
    private String title;
    @Schema(example = "Deneme")
    private String body;
    @JsonIgnore
    @Schema(example = "notification")
    private String topic;
    @JsonIgnore
    private String token;
}
