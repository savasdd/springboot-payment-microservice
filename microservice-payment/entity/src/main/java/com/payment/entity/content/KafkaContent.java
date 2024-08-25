package com.payment.entity.content;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KafkaContent implements Serializable {

    private String eventType;
    private String aggregateId;
    private byte[] data;

}
