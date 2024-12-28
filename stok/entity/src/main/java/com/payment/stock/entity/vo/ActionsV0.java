package com.payment.stock.entity.vo;

import com.payment.stock.common.enums.ActionType;
import com.payment.stock.entity.base.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class ActionsV0 extends BaseDto implements Serializable {

    @Schema(example = "1")
    private Long actionId;
    @Schema(example = "23")
    private Long userId;
    @Schema(example = "LIKE")
    private ActionType actionType;
    @Schema(example = ":)")
    private String emotion;
}
