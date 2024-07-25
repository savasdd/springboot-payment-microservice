package com.payment.user.service.impl;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.common.utils.BeanUtil;
import com.payment.user.entity.dto.UserDto;
import com.payment.user.entity.model.User;
import com.payment.user.entity.vo.UserVo;
import com.payment.user.repository.CityRepository;
import com.payment.user.repository.UserRepository;
import com.payment.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final CityRepository cityRepository;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse findAll(Pageable pageable) {
        Page<UserDto> list = beanUtil.mapAll(repository.findAll(pageable), UserDto.class);

        log.info("get all user {}", list.getTotalElements());
        return BaseResponse.builder().data(list.getContent()).count((int) list.getTotalElements()).build();
    }

    @Override
    public BaseResponse findById(Long id) {
        return BaseResponse.builder().data(beanUtil.mapDto(repository.findById(id).orElse(null), UserDto.class)).build();
    }

    @Override
    public BaseResponse save(UserVo dto) {
        User user = beanUtil.mapDto(dto, User.class);
        user.setCity(cityRepository.findById(dto.getCity().getId()).orElse(null));
        User model = repository.save(user);

        log.info("save user: {}", model);
        return BaseResponse.builder().data(beanUtil.mapDto(model, UserDto.class)).build();
    }

    @Override
    public BaseResponse update(Long id, UserVo dto) {
        User user = beanUtil.transform(dto, repository.findById(id).orElse(null));
        user.setCity(cityRepository.findById(dto.getCity().getId()).orElse(null));

        User model = repository.save(user);

        log.info("update user: {}", model);
        return BaseResponse.builder().data(beanUtil.mapDto(model, UserDto.class)).build();
    }

    @Override
    public BaseResponse delete(Long id) {
        repository.deleteById(id);

        log.info("delete user: {}", id);
        return BaseResponse.builder().build();
    }
}
