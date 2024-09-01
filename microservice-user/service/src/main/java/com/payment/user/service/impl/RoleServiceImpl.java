package com.payment.user.service.impl;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.common.utils.BeanUtil;
import com.payment.user.entity.dto.RoleDto;
import com.payment.user.entity.model.City;
import com.payment.user.entity.model.Role;
import com.payment.user.repository.CityRepository;
import com.payment.user.repository.RoleRepository;
import com.payment.user.service.CityService;
import com.payment.user.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse getById(Long id) {
        return BaseResponse.builder().data(repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()))).build();
    }

    @Override
    public BaseResponse findAll(Pageable pageable) {
        List<Role> list = repository.findAll(pageable).getContent();
        log.info("getAll role list size: {}", list.size());

        return BaseResponse.builder().data(beanUtil.mapAll(list, RoleDto.class)).count(list.size()).build();
    }

    @Override
    public BaseResponse save(RoleDto role) {
        Role model = repository.save(beanUtil.mapDto(role, Role.class));
        log.info("save role: {}", model);

        return BaseResponse.builder().data(beanUtil.mapDto(model, RoleDto.class)).build();
    }

    @Override
    public BaseResponse update(Long id, RoleDto roleDto) {
        Role model = beanUtil.transform(roleDto, repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString())));
        model.setId(id);
        repository.save(model);

        log.info("update role: {}", model);
        return BaseResponse.builder().data(beanUtil.mapDto(model, RoleDto.class)).build();
    }

    @Override
    public BaseResponse delete(Long id) {
        repository.deleteById(id);
        log.info("delete role: {}", id);
        return BaseResponse.builder().build();
    }
}
