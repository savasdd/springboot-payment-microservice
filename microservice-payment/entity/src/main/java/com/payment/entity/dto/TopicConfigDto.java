package com.payment.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicConfigDto {
    private String name;
    private Integer partitions = 1;
    private Integer replication = 1;
}
