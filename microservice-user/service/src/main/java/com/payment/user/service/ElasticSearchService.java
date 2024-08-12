package com.payment.user.service;

import com.payment.user.common.base.BaseResponse;
import org.springframework.data.domain.Pageable;

public interface ElasticSearchService {

    BaseResponse searchUser(String searchIndex, Pageable searchPage);
    BaseResponse searchPerson(String searchIndex, String language, Pageable searchPage);
}
