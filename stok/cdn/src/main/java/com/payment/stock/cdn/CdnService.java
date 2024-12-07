package com.payment.stock.cdn;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CdnService {

    BaseResponse fileUpload(Long stockId, MultipartFile file);

    ResponseEntity<byte[]> getImageByte(Long stockId);

    BaseResponse getImage(Long stockId);

    BaseResponse getAll();

    BaseResponse delete(Long stockId);

    BaseResponse getAllLoad(DataLoad load);
}
