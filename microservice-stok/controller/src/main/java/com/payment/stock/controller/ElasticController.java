package com.payment.stock.controller;

import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.elastic.IndexService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/elastic")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ElasticController {
    private IndexService indexService;

    @GetMapping(value = "/index")
    public ResponseEntity<BaseResponse> index() {
        return ResponseEntity.ok(indexService.index());
    }
}
