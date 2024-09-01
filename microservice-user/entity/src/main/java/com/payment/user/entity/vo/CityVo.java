package com.payment.user.entity.vo;

import com.payment.user.entity.base.BaseDto;
import com.payment.user.entity.dto.RoleDto;
import com.payment.user.entity.model.City;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CityVo implements Serializable {

    private Long id;
}
