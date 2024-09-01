package com.payment.user.service.jwt;

import com.payment.user.entity.dto.LoginDto;
import com.payment.user.entity.dto.TokenDto;
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

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    @Override
    public TokenDto authenticate(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(EntityNotFoundException::new);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        String token = jwtService.generateToken(user);
        TokenDto response = TokenDto.builder().token(token).expiresIn(jwtService.getExpirationTime()).build();

        log.info("Authenticated user: {} {} {}", user.getFirstName(), user.getLastName(), user.getUsername());
        return response;
    }
}
