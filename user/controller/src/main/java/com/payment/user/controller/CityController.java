package com.payment.user.controller;

import com.load.impl.DataLoad;
import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.model.City;
import com.payment.user.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/users/city")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CityController {

    private final CityService cityService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(cityService.findAll());
    }

    @GetMapping(value = "/all-pageable")
    public ResponseEntity<BaseResponse> findAllPageable(Pageable pageable) {
        return ResponseEntity.ok(cityService.findAllPageable(pageable));
    }

    @PostMapping(value = "/all-load")
    public ResponseEntity<com.load.base.BaseResponse> findAllLoad(@RequestBody DataLoad dataLoad) {
        return ResponseEntity.ok(cityService.findAllLoad(dataLoad));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getCityById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody City city) {
        return new ResponseEntity<>(cityService.saveCity(city), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BaseResponse> update(@Validated @RequestBody City city) {
        return new ResponseEntity<>(cityService.updateCity(city), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(cityService.deleteCity(id), HttpStatus.OK);
    }
}
