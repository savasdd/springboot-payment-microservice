package com.payment.user.service.jwt;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.dto.LoginDto;
import com.payment.user.entity.dto.TokenDto;
import com.payment.user.entity.dto.TokenVo;
import com.payment.user.entity.model.Role;
import com.payment.user.entity.model.User;
import com.payment.user.repository.UserRepository;
import com.payment.user.service.AuthService;
import com.payment.user.service.JWTService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    @Override
    public TokenDto authenticate(LoginDto loginDto) {
        TokenDto response = null;
        User user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(()->new EntityNotFoundException("User not found"));

        if (Objects.isNull(user.getToken()) || jwtService.validateExpiration(user.getToken())) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword(), user.getAuthorities()));

            String token = jwtService.generateToken(user);
            user.setToken(token);
            userRepository.save(user);
            response = TokenDto.builder().token(token).expiresIn(jwtService.getExpirationTime()).roles(getRoles(user.getRoles())).build();
        } else
            response = TokenDto.builder().token(user.getToken()).expiresIn(jwtService.getExpirationTime()).roles(getRoles(user.getRoles())).build();


        log.info("Authenticated user: {} {} - {}", user.getFirstName(), user.getLastName(), user.getUsername());
        return response;
    }

    @Override
    public BaseResponse validate(TokenVo vo) {
        return BaseResponse.success(!jwtService.validateExpiration(vo.getToken()));
    }

    private List<String> getRoles(List<Role> roles) {
        return roles.stream().map(Role::getName).toList();
    }
}
