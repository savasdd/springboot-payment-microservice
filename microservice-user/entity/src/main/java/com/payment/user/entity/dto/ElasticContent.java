package com.payment.user.entity.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElasticContent implements Serializable {

    private Long id;
    private String firstName;
    private String street;
    private String language;
}
