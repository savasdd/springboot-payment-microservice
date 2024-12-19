package com.payment.stock.controller;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.CategoryDto;
import com.payment.stock.entity.dto.StockRateDto;
import com.payment.stock.entity.model.Category;
import com.payment.stock.service.CategoryService;
import com.payment.stock.service.RateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/stocks/category")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }


    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> findAllLoad(@RequestBody DataLoad load) {
        return ResponseEntity.ok(categoryService.findAllLoad(load));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody CategoryDto dto) {
        return new ResponseEntity<>(categoryService.save(dto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BaseResponse> update(@RequestBody CategoryDto dto) {
        return new ResponseEntity<>(categoryService.update(dto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.delete(id), HttpStatus.OK);
    }


}
