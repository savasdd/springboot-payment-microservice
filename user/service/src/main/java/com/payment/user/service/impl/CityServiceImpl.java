package com.payment.user.service.impl;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.user.common.base.BaseResponse;
import com.payment.user.common.utils.BeanUtil;
import com.payment.user.entity.dto.CityDto;
import com.payment.user.entity.model.City;
import com.payment.user.entity.model.User;
import com.payment.user.repository.CityRepository;
import com.payment.user.service.CityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse getCityById(Long id) {
        return BaseResponse.success(cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString())));
    }

    @Override
    public BaseResponse findAll() {
        List<City> cityList = cityRepository.findAll();
        log.info("getAll city list size: {}", cityList.size());
        return BaseResponse.success(cityList, (long) cityList.size());
    }

    @Override
    public BaseResponse findAllPageable(Pageable pageable) {
        Page<City> cityList = cityRepository.findAll(pageable);
        log.info("getAll city list size: {}", cityList.getTotalElements());
        return BaseResponse.success(cityList, cityList.getTotalElements());
    }

    @Override
    public BaseResponse findAllLoad(DataLoad dataLoad) {
        BaseLoadResponse response = cityRepository.load(dataLoad);
        log.info("Load city list size: {}", response.getTotalCount());
        return BaseResponse.success(beanUtil.mapAll(response.getData(), City.class, CityDto.class), response.getTotalCount());
    }

    @Override
    public BaseResponse saveCity(City city) {
        City cityEntity = cityRepository.save(city);
        log.info("save city: {}", cityEntity);

        return BaseResponse.success(cityEntity);
    }

    @Override
    public BaseResponse updateCity(City city) {
        City model = beanUtil.transform(city, cityRepository.findById(city.getId()).orElseThrow(() -> new EntityNotFoundException("City Not Found")));
        cityRepository.save(model);

        log.info("update city: {}", model);
        return BaseResponse.success(model);
    }

    @Override
    public BaseResponse deleteCity(Long id) {
        cityRepository.deleteById(id);
        log.info("delete city: {}", id);
        return BaseResponse.success("delete success");
    }
}
