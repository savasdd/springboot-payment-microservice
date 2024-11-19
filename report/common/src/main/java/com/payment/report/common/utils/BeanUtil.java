package com.payment.report.common.utils;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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


}
