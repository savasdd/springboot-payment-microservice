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
public class PersonDto implements Serializable {

    private Long id;
    private String firstName;
    private Collection<HouseDto> houses = new ArrayList<>();
}
