package com.payment.stock.service.impl;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.entity.dto.PropertyDto;
import com.payment.stock.entity.model.Property;
import com.payment.stock.repository.PropertyRepository;
import com.payment.stock.service.PropertyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse findAll() {
        List<Property> dtoList = propertyRepository.findAll();

        log.info("find all property: {}", dtoList.size());
        return BaseResponse.success(beanUtil.mapAll(dtoList, PropertyDto.class), (long) dtoList.size());
    }

    @Override
    public BaseResponse findAllLoad(DataLoad load) {
        BaseLoadResponse response = propertyRepository.load(load);
        List<PropertyDto> stockDtoList = beanUtil.mapAll(response.getData(), Property.class, PropertyDto.class);

        log.info("Load all property: {}", response.getTotalCount());
        return BaseResponse.success(stockDtoList, response.getTotalCount());
    }


    @Override
    public BaseResponse findById(Long id) {
        Property model = propertyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return BaseResponse.success(beanUtil.mapDto(model, PropertyDto.class));
    }

    @Override
    public BaseResponse save(PropertyDto dto) {
        Property property = beanUtil.mapDto(dto, Property.class);
        Property model = propertyRepository.save(property);

        log.info("save property: {}", model);
        return BaseResponse.success(model);
    }


    @Override
    public BaseResponse update(PropertyDto dto) {
        Property property = propertyRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        updateField(dto, property);

        propertyRepository.save(property);
        log.info("update property: {}", property);
        return BaseResponse.success(property);
    }

    @Override
    public BaseResponse delete(Long id) {
        Property property = propertyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        property.setRecordStatus(RecordStatus.DELETED);
        Property model = propertyRepository.save(property);

        log.info("delete property: {}", model);
        return BaseResponse.success(model);
    }

    private void updateField(PropertyDto dto, Property model) {
        model.setProperty(Objects.isNull(dto.getProperty()) ? model.getProperty() : dto.getProperty());
        model.setRecordStatus(Objects.isNull(dto.getRecordStatus()) ? model.getRecordStatus() : dto.getRecordStatus());
    }

}
