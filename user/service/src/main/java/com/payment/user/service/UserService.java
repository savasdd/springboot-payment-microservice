package com.payment.user.service;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.dto.UserDto;
import com.payment.user.entity.vo.UserVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    BaseResponse findAll();

    BaseResponse findAllPageable(Pageable pageable);

    List<UserDto> findAllDto(Pageable pageable);

    BaseResponse findById(Long id);

    BaseResponse save(UserVo user);

    BaseResponse update(UserVo user);

    BaseResponse delete(Long id);
}
