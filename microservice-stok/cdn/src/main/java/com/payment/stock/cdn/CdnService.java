package com.payment.stock.cdn;

import com.payment.stock.common.base.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CdnService {

    BaseResponse fileUpload(Long stockId, MultipartFile file);
}
