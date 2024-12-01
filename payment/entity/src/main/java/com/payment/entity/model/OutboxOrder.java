package com.payment.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OUTBOX_ORDERS")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutboxOrder extends BaseEntity implements Serializable {

    @Column(name = "eventType", nullable = false)
    private String eventType;
    @Column(name = "aggregateId")
    private Long aggregateId;
    @Column(name = "data")
    private byte[] data;

}
