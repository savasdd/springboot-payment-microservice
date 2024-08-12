package com.payment.user.controller;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.elastic.IndexService;
import com.payment.user.elastic.SearchService;
import com.payment.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/elastic")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ElasticController {

    private final IndexService indexService;
    private final SearchService searchService;

    @PostMapping(value = "/index")
    public ResponseEntity<BaseResponse> indexUser() {
        indexService.indexUser();
        return ResponseEntity.ok(BaseResponse.builder().data("Success").build());
    }

    @GetMapping(value = "/search/{text}")
    public ResponseEntity<BaseResponse> searchUser(@PathVariable String text, Pageable pageable) {
        return ResponseEntity.ok(searchService.searchUser(text, pageable));
    }

}
