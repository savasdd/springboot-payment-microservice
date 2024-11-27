package com.payment.user.service;

import com.load.impl.DataLoad;
import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.model.City;
import org.springframework.data.domain.Pageable;

public interface CityService {
    BaseResponse getCityById(Long id);

    BaseResponse findAll();

    BaseResponse findAllPageable(Pageable pageable);

    com.load.base.BaseResponse findAllLoad(DataLoad dataLoad);

    BaseResponse saveCity(City city);

    BaseResponse updateCity(City city);

    BaseResponse deleteCity(Long id);
}
