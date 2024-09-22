package com.payment.report.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
