package com.payment.notification.application.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaTopicsConfigDto {
    private String name;
    private Integer partitions = 1;
    private Integer replication = 1;
}