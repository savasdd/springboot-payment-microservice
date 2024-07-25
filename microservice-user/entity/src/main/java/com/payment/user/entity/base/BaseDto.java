package com.payment.user.entity.base;

import com.payment.user.common.enums.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
public abstract class BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1656703245648711747L;

    private Long id;
    private Date createdDate;
    private Date updatedDate;
    private RecordStatus recordStatus;
    private String createdBy;
    private String updatedBy;

}