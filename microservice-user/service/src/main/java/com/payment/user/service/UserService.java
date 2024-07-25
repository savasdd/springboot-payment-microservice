package com.payment.user.service;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.vo.UserVo;
import org.springframework.data.domain.Pageable;

public interface UserService {
    BaseResponse findAll(Pageable pageable);

    BaseResponse findById(Long id);

    BaseResponse save(UserVo user);

    BaseResponse update(Long id, UserVo user);

    BaseResponse delete(Long id);
}
