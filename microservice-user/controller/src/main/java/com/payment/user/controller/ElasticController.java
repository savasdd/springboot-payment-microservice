package com.payment.user.controller;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.service.ElasticIndexService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/elastic")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ElasticController {
    private final ElasticIndexService elasticService;

    @PostMapping(value = "/index")
    public ResponseEntity<BaseResponse> indexUser() {
        elasticService.indexUser();
        return ResponseEntity.ok(BaseResponse.builder().data("Success").build());
    }

}
