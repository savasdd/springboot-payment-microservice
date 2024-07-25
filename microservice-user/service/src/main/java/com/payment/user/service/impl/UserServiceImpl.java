package com.payment.user.service.impl;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.common.util.BeanUtil;
import com.payment.user.entity.dto.UserDto;
import com.payment.user.entity.model.User;
import com.payment.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final List<User> users = new ArrayList<>();
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse findAll() {
        return BaseResponse.builder().data(beanUtil.mapAll(users, UserDto.class)).count(users.size()).build();
    }

    @Override
    public BaseResponse findById(Long id) {
        return null;
    }

    @Override
    public BaseResponse save(User user) {
        users.add(user);
        log.info("save user: {}", user);
        return BaseResponse.builder().data(user).build();
    }

    @Override
    public BaseResponse update(User user) {
        return null;
    }

    @Override
    public BaseResponse delete(Long id) {
        return null;
    }
}
