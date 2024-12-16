package com.payment.stock.elastic;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    BaseResponse search(String searchText, DataLoad load);
}
