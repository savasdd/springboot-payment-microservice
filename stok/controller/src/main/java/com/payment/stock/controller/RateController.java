package com.payment.stock.controller;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.StockRateDto;
import com.payment.stock.service.RateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/stocks/rate")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RateController {

    private final RateService rateService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(rateService.findAll());
    }


    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> findAllLoad(@RequestBody DataLoad load) {
        return ResponseEntity.ok(rateService.findAllLoad(load));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rateService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody StockRateDto dto) {
        return new ResponseEntity<>(rateService.save(dto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BaseResponse> update(@RequestBody StockRateDto dto) {
        return new ResponseEntity<>(rateService.update(dto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(rateService.delete(id), HttpStatus.OK);
    }


}
