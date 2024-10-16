package com.payment.stock.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payment.stock.common.enums.RecordStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public abstract class BaseDto implements Serializable {

    private Long id;
    private Date createdDate;
    private Date updatedDate;
    private RecordStatus recordStatus;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String updatedBy;

}