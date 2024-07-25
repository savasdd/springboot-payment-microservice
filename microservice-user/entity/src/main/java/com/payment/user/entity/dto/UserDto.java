package com.payment.user.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payment.user.entity.base.BaseDto;
import com.payment.user.entity.model.City;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto extends BaseDto implements Serializable {

    private String username;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private City city;
}
