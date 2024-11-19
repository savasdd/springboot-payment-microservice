package com.payment.stock.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payment.stock.entity.base.BaseDto;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class UserDto extends BaseDto implements Serializable {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
