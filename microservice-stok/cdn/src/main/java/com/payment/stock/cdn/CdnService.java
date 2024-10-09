package com.payment.stock.cdn;

import com.payment.stock.common.base.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CdnService {

    BaseResponse fileUpload(Long stockId, MultipartFile file);

    ResponseEntity<byte[]> getImage(Long stockId);

    BaseResponse getAll();

    BaseResponse delete(Long stockId);
}
