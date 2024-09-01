package com.payment.user.service;

import com.payment.user.entity.dto.LoginDto;
import com.payment.user.entity.dto.TokenDto;

public interface AuthService {

    TokenDto authenticate(LoginDto loginDto);
}
