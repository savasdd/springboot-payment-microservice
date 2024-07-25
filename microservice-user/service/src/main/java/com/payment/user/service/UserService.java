package com.payment.user.service;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.model.User;

public interface UserService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse save(User user);

    BaseResponse update(User user);

    BaseResponse delete(Long id);
}
