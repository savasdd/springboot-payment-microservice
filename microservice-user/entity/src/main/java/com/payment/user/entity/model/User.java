package com.payment.user.entity.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
