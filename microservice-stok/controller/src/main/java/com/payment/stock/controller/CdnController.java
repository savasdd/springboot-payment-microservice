package com.payment.stock.controller;

import com.payment.stock.cdn.CdnService;
import com.payment.stock.common.base.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/payment/cdn")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CdnController {
    private final CdnService cdnService;

    @PostMapping(value = "/upload/{stockId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadFile(@PathVariable("stockId") Long stockId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(cdnService.fileUpload(stockId, file));
    }
}
