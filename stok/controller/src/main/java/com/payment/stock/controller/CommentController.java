package com.payment.stock.controller;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.utils.HeaderUtil;
import com.payment.stock.entity.vo.CommentV0;
import com.payment.stock.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<BaseResponse> findAllLoad(HttpServletRequest request, @RequestBody DataLoad load) {
        return ResponseEntity.ok(commentService.findAllLoad(load, HeaderUtil.getUserId(request)));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(HttpServletRequest request, @RequestBody CommentV0 dto) {
        return new ResponseEntity<>(commentService.save(dto, HeaderUtil.getUserId(request)), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BaseResponse> update(HttpServletRequest request, @RequestBody CommentV0 dto) {
        Long userId = HeaderUtil.getUserId(request);
        return new ResponseEntity<>(commentService.update(dto, userId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(HttpServletRequest request, @PathVariable Long id) {
        return new ResponseEntity<>(commentService.delete(id, HeaderUtil.getUserId(request)), HttpStatus.OK);
    }


}
