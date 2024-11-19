package com.payment.user.elastic;

import com.payment.user.common.base.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    BaseResponse searchUser(String searchText, Pageable searchPage);
}
