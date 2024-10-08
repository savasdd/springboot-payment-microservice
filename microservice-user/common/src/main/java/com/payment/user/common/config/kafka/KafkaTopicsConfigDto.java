package com.payment.user.common.config.kafka;

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