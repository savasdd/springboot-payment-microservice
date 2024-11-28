package com.payment.user.controller;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.vo.UserVo;
import com.payment.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/users")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping(value = "/pageable")
    public ResponseEntity<BaseResponse> findAllPageable(Pageable pageable) {
        return ResponseEntity.ok(userService.findAllPageable(pageable));
    }

    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> findAllLoad(@RequestBody DataLoad dataLoad) {
        return ResponseEntity.ok(userService.findAllLoad(dataLoad));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping(path = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody UserVo user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BaseResponse> update(@RequestBody UserVo user) {
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(userService.delete(id), HttpStatus.OK);
    }
}
