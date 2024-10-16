package com.payment.user.service;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.dto.LoginDto;
import com.payment.user.entity.dto.TokenDto;
import com.payment.user.entity.dto.TokenVo;

public interface AuthService {

    TokenDto authenticate(LoginDto loginDto);

    BaseResponse validate(TokenVo vo);
}
