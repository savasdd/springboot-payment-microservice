package com.payment.stock.controller;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.PropertyDto;
import com.payment.stock.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/stocks/property")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(propertyService.findAll());
    }

    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> findAllLoad(@RequestBody DataLoad load) {
        return ResponseEntity.ok(propertyService.findAllLoad(load));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody PropertyDto dto) {
        return new ResponseEntity<>(propertyService.save(dto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BaseResponse> update(@RequestBody PropertyDto dto) {
        return new ResponseEntity<>(propertyService.update(dto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(propertyService.delete(id), HttpStatus.OK);
    }


}
