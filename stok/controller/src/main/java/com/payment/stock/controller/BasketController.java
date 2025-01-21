package com.payment.stock.controller;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.utils.HeaderUtil;
import com.payment.stock.entity.vo.BasketV0;
import com.payment.stock.service.BasketService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/payment/stocks/basket")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BasketController {

    private final BasketService basketService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(basketService.findAll());
    }

    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> findAllLoad(HttpServletRequest request, @RequestBody DataLoad load) {
        Long userId = HeaderUtil.getUserId(request);
        return ResponseEntity.ok(basketService.findAllLoad(load, userId));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(basketService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(HttpServletRequest request, @RequestBody BasketV0 dto) {
        Long userId = HeaderUtil.getUserId(request);
        return new ResponseEntity<>(basketService.save(dto, userId), HttpStatus.CREATED);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> update(@RequestBody List<Long> idList) {
        basketService.update(idList);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(HttpServletRequest request, @PathVariable Long id) {
        Long userId = HeaderUtil.getUserId(request);
        return new ResponseEntity<>(basketService.delete(id, userId), HttpStatus.OK);
    }


}
