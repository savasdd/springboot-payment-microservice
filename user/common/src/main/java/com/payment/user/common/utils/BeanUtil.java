package com.payment.user.common.utils;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeanUtil {

    private final ModelMapper modelMapper;

    public <T> T mapDto(Object model, Class<T> dto) {
        return model != null ? modelMapper.map(model, dto) : null;
    }

    public <T> T transform(Object model, Object dto) {
        modelMapper.map(model, dto);
        return (T) dto;
    }

    public <D, T> List<D> mapAll(final Collection<T> list, final Class<D> dto) {
        return list.stream().map(val -> mapDto(val, dto)).toList();
    }

    public <D, M, T> List<D> mapAll(final Object items, final Class<M> model, final Class<D> dto) {
        List<M> modelList = JsonUtil.fromJsonList(items, model);
        return modelList.stream().map(val -> mapDto(val, dto)).toList();
    }

    public <D, T> Page<D> mapAll(final Page<T> list, final Class<D> dto) {
        List<D> dtoList = list.stream().map(val -> mapDto(val, dto)).toList();
        return new PageImpl<>(dtoList, list.getPageable(), list.getTotalElements());
    }


}
