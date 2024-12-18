package com.payment.stock.controller;

import com.load.impl.DataLoad;
import com.payment.stock.cdn.CdnService;
import com.payment.stock.common.base.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/payment/stocks/cdn")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CdnController {
    private final CdnService cdnService;

    @GetMapping(value = "/all")
    public ResponseEntity<BaseResponse> getAll() {
        return ResponseEntity.ok(cdnService.getAll());
    }

    @PostMapping(value = "/pageable-load")
    public ResponseEntity<BaseResponse> getAllLoad(@RequestBody DataLoad load) {
        return ResponseEntity.ok(cdnService.getAllLoad(load));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadFile(@RequestParam("stockId") Long stockId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(cdnService.fileUpload(stockId, file));
    }

    @GetMapping(value = "/image-byte")
    public ResponseEntity<byte[]> getImageByte(@RequestParam("stockId") Long stockId) {
        return cdnService.getImageByte(stockId);
    }

    @GetMapping(value = "/image")
    public ResponseEntity<BaseResponse> getImage(@RequestParam("stockId") Long stockId) {
        return ResponseEntity.ok(cdnService.getImage(stockId));
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse> delete(@RequestParam("stockId") Long stockId) {
        return ResponseEntity.ok(cdnService.delete(stockId));
    }
}
