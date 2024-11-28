package com.payment.user.service.impl;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.user.common.base.BaseResponse;
import com.payment.user.common.exception.GeneralException;
import com.payment.user.common.utils.BeanUtil;
import com.payment.user.entity.base.ValidationDto;
import com.payment.user.entity.dto.RoleDto;
import com.payment.user.entity.model.Role;
import com.payment.user.entity.vo.RoleVo;
import com.payment.user.repository.RoleRepository;
import com.payment.user.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse getById(Long id) {
        return BaseResponse.success(repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString())));
    }

    @Override
    public BaseResponse findAll() {
        List<Role> list = repository.findAll();
        log.info("getAll role list size: {}", list.size());
        return BaseResponse.success(beanUtil.mapAll(list, RoleDto.class), (long) list.size());
    }

    @Override
    public BaseResponse findAllPageable(Pageable pageable) {
        Page<Role> list = repository.findAll(pageable);
        log.info("getAll role list size: {}", list.getTotalElements());

        return BaseResponse.success(beanUtil.mapAll(list, RoleDto.class), list.getTotalElements());
    }

    @Override
    public BaseResponse save(RoleVo role) {
        ValidationDto valid = validation(role);
        if (valid.isError()) {
            throw new RuntimeException(valid.getMessage());
        }

        Role model = repository.save(beanUtil.mapDto(role, Role.class));
        log.info("save role: {}", model);

        return BaseResponse.success(beanUtil.mapDto(model, RoleDto.class));
    }

    @Override
    public BaseResponse update(RoleVo vo) {
        ValidationDto valid = validation(vo);
        if (valid.isError()) {
            throw new RuntimeException(valid.getMessage());
        }
        Role model = beanUtil.transform(vo, repository.findById(vo.getId()).orElseThrow(() -> new EntityNotFoundException("Role not found")));
        repository.save(model);

        log.info("update role: {}", model);
        return BaseResponse.success(beanUtil.mapDto(model, RoleDto.class));
    }

    @Override
    public BaseResponse delete(Long id) {
        repository.deleteById(id);
        log.info("delete role: {}", id);
        return BaseResponse.success("delete role success");
    }

    @Override
    public BaseResponse findAllLoad(DataLoad dataLoad) {
        BaseLoadResponse response = repository.load(dataLoad);
        log.info("Load role list size: {}", response.getTotalCount());
        return BaseResponse.success(beanUtil.mapAll(response.getData(), Role.class, RoleDto.class), response.getTotalCount());
    }

    private ValidationDto validation(RoleVo vo) {
        if (Objects.isNull(vo.getName()))
            return ValidationDto.validation(true, "Rol Name is required");

        return ValidationDto.validation(false, "Success");

    }

}
