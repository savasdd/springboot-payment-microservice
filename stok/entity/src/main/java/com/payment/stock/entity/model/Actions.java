package com.payment.stock.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.ActionType;
import com.payment.stock.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ACTIONS")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Actions extends BaseEntity implements Serializable {

    @Column(name = "actionId")
    private Long actionId;

    @Column(name = "userId")
    private Long userId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "actionType")
    private ActionType actionType;

    @Column(name = "emotion")
    private String emotion;
}
