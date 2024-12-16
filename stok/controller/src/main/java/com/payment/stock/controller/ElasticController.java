package com.payment.stock.controller;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.elastic.IndexService;
import com.payment.stock.elastic.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/stocks/elastic")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ElasticController {
    private final IndexService indexService;
    private final SearchService searchService;

    @GetMapping(value = "/index")
    public ResponseEntity<BaseResponse> index() {
        return ResponseEntity.ok(indexService.index());
    }

    @PostMapping(value = "/search")
    public ResponseEntity<BaseResponse> search(@RequestParam(value = "text", required = false) String text, @RequestBody DataLoad load) {
        return ResponseEntity.ok(searchService.search(text, load));
    }
}
