package com.payment.user.controller;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.dto.RoleDto;
import com.payment.user.entity.model.City;
import com.payment.user.service.CityService;
import com.payment.user.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/users/role")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    private final RoleService roleService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll(Pageable pageable) {
        return ResponseEntity.ok(roleService.findAll(pageable));
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getById(id));
    }

    @PostMapping(value = "/save")
    public ResponseEntity<BaseResponse> save(@RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.save(roleDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<BaseResponse> update(@Validated @PathVariable Long id, @RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.update(id, roleDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.delete(id), HttpStatus.OK);
    }
}
