package com.payment.stock.controller;

import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.StockDto;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/stocks")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StockController {

    private final StockService stockService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll(Pageable pageable) {
        return ResponseEntity.ok(stockService.findAll(pageable));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody StockDto stock) {
        return new ResponseEntity<>(stockService.save(stock), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<BaseResponse> update(@PathVariable Long id, @RequestBody StockDto stock) {
        return new ResponseEntity<>(stockService.update(id, stock), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(stockService.delete(id), HttpStatus.OK);
    }

}
