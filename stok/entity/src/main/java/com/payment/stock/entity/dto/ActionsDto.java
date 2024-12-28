package com.payment.stock.entity.dto;

import com.payment.stock.common.enums.ActionType;
import com.payment.stock.entity.base.BaseDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class ActionsDto extends BaseDto implements Serializable {

    private Long actionId;
    private Long userId;
    private ActionType actionType;
    private String emotion;
}
