package com.payment.user.service;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.model.City;
import org.springframework.data.domain.Pageable;

public interface CityService {
    BaseResponse getCityById(Long id);

    BaseResponse findAll(Pageable pageable);

    BaseResponse saveCity(City city);

    BaseResponse updateCity(Long id, City city);

    BaseResponse deleteCity(Long id);
}
