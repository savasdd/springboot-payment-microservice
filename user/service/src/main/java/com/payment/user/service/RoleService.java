package com.payment.user.service;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.vo.RoleVo;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    BaseResponse getById(Long id);

    BaseResponse findAll();

    BaseResponse findAllPageable(Pageable pageable);

    BaseResponse save(RoleVo role);

    BaseResponse update(RoleVo role);

    BaseResponse delete(Long id);

    BaseResponse findAllLoad(DataLoad dataLoad);
}
