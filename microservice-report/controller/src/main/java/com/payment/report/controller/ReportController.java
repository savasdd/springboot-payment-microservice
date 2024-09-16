package com.payment.report.controller;

import com.payment.report.common.base.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/payment/reports")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ReportController {

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> findAll() {
        return ResponseEntity.ok(BaseResponse.builder().data("Report Success").build());
    }
}
