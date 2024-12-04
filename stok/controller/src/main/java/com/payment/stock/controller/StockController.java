package com.payment.stock.controller;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.StockDto;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/payment/stocks")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StockController {

    private final StockService stockService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping(value = "/pageable")
    public ResponseEntity<BaseResponse> findPageable(Pageable pageable) {
        return ResponseEntity.ok(stockService.findPageable(pageable));
    }

    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> findAllLoad(@RequestBody DataLoad load) {
        return ResponseEntity.ok(stockService.findAllLoad(load));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody StockDto stock) {
        return new ResponseEntity<>(stockService.save(stock), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BaseResponse> update(@RequestBody StockDto stock) {
        return new ResponseEntity<>(stockService.update(stock), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(stockService.delete(id), HttpStatus.OK);
    }

    @GetMapping(value = "/update-quantity/{id}")
    public ResponseEntity<BaseResponse> updateStockQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        return new ResponseEntity<>(stockService.updateStockQuantity(id, quantity), HttpStatus.OK);
    }

    @PostMapping(value = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadExcel(@RequestParam("userId") Long userId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(stockService.uploadExcel(userId, file));
    }

}
