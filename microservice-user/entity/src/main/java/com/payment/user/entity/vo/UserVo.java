package com.payment.user.entity.vo;

import com.payment.user.entity.base.BaseDto;
import com.payment.user.entity.model.City;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class UserVo extends BaseDto implements Serializable {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private City city;
}
