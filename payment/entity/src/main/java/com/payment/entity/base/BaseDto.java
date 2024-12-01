package com.payment.entity.base;

import com.payment.common.enums.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
public abstract class BaseDto implements Serializable {

    private Long id;
    private Date creDate;
    private RecordStatus recordStatus;
    private String creBy;

}