package com.payment.stock.controller;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.vo.CommentV0;
import com.payment.stock.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/stocks/comment")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(commentService.findAll());
    }

    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> findAllLoad(@RequestBody DataLoad load) {
        return ResponseEntity.ok(commentService.findAllLoad(load));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody CommentV0 dto) {
        return new ResponseEntity<>(commentService.save(dto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BaseResponse> update(@RequestBody CommentV0 dto) {
        return new ResponseEntity<>(commentService.update(dto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.delete(id), HttpStatus.OK);
    }


}
