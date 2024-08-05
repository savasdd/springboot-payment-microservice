package com.payment.user.controller;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.entity.vo.UserVo;
import com.payment.user.service.ElasticService;
import com.payment.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/payment/elastic")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ElasticController {
    private final ElasticService elasticService;

    @PostMapping(value = "/index")
    public ResponseEntity<BaseResponse> indexUser() {
        elasticService.indexUser();
        return ResponseEntity.ok(BaseResponse.builder().data("Success").build());
    }

}
