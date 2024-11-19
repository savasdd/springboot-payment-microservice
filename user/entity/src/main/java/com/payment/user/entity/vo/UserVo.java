package com.payment.user.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserVo implements Serializable {

    private Long id;
    @Schema(example = "svsdd")
    private String username;
    @Schema(example = "123")
    private String password;
    @Schema(example = "Savas")
    private String firstName;
    @Schema(example = "Dede")
    private String lastName;
    @Schema(example = "svsdd@gmail.com")
    private String email;
    @Schema(example = "Ankara")
    private String address;
    private CityVo city;
    private List<RoleVo> roles;
}
