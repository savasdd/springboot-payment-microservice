package com.payment.stock.controller;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.vo.ActionsV0;
import com.payment.stock.service.ActionsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/stocks/actions")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ActionsController {

    private final ActionsService actionsService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(actionsService.findAll());
    }

    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> findAllLoad(@RequestBody DataLoad load) {
        return ResponseEntity.ok(actionsService.findAllLoad(load));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(actionsService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody ActionsV0 dto) {
        return new ResponseEntity<>(actionsService.save(dto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BaseResponse> update(@RequestBody ActionsV0 dto) {
        return new ResponseEntity<>(actionsService.update(dto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(actionsService.delete(id), HttpStatus.OK);
    }


}
