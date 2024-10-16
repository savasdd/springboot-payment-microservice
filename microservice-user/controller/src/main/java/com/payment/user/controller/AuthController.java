package com.payment.user.controller;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.dto.LoginDto;
import com.payment.user.entity.dto.TokenDto;
import com.payment.user.entity.dto.TokenVo;
import com.payment.user.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/users/auth")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.authenticate(loginDto));
    }

    @PostMapping(path = "/valid-token")
    public ResponseEntity<BaseResponse> validate(@RequestBody TokenVo vo) {
        return ResponseEntity.ok(authService.validate(vo));
    }


}
