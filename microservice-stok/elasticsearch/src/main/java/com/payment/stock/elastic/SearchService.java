package com.payment.stock.elastic;

import com.payment.stock.common.base.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    BaseResponse search(String searchText, Pageable pageable);
}
