package com.payment.user.controller;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.service.ElasticIndexService;
import com.payment.user.service.ElasticSearchService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/elastic")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ElasticController {
    private final ElasticIndexService elasticService;
    private final ElasticSearchService searchService;

    @PostMapping(value = "/index")
    public ResponseEntity<BaseResponse> indexUser() {
        elasticService.indexUser();
        return ResponseEntity.ok(BaseResponse.builder().data("Success").build());
    }

    @GetMapping(value = "/search/{index}")
    public ResponseEntity<BaseResponse> searchUser(@PathVariable String index, Pageable pageable) {
        return ResponseEntity.ok(searchService.searchUser(index, pageable));
    }

}
