package com.payment.user.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payment.user.common.enums.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1656703245648711747L;

    private Long id;
    private RecordStatus recordStatus;
    @JsonIgnore
    private String updatedBy;

}