package com.payment.user.entity.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElasticUserDto implements Serializable {

    private Long id;
    private String contentType;
    private Integer contentId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
}
