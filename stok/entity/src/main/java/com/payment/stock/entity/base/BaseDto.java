package com.payment.stock.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.payment.stock.common.enums.RecordStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseDto implements Serializable {

    private Long id;
    private Date creDate;
    private RecordStatus recordStatus;
    @JsonIgnore
    private String creBy;

}