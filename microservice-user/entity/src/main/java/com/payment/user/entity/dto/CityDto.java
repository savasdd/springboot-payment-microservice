package com.payment.user.entity.dto;

import com.payment.user.entity.base.BaseDto;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityDto extends BaseDto implements Serializable {

    private String name;
}
