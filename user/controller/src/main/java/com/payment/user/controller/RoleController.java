package com.payment.user.controller;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.vo.RoleVo;
import com.payment.user.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/users/role")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    private final RoleService roleService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping(value = "/pageable")
    public ResponseEntity<BaseResponse> findAllPageable(Pageable pageable) {
        return ResponseEntity.ok(roleService.findAllPageable(pageable));
    }

    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> findAllLoad(@RequestBody DataLoad dataLoad) {
        return ResponseEntity.ok(roleService.findAllLoad(dataLoad));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody RoleVo roleDto) {
        return new ResponseEntity<>(roleService.save(roleDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<BaseResponse> update(@RequestBody RoleVo roleDto) {
        return new ResponseEntity<>(roleService.update(roleDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.delete(id), HttpStatus.OK);
    }
}
