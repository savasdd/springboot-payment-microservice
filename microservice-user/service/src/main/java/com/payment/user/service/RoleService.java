package com.payment.user.service;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.dto.RoleDto;
import com.payment.user.entity.model.City;
import com.payment.user.entity.model.Role;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    BaseResponse getById(Long id);

    BaseResponse findAll(Pageable pageable);

    BaseResponse save(RoleDto role);

    BaseResponse update(Long id, RoleDto role);

    BaseResponse delete(Long id);
}
