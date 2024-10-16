package com.payment.gateway.common.filter;

import com.payment.gateway.common.utils.RestUtil;
import org.springframework.stereotype.Component;

@Component
public class JwtFilter extends FilterFactory {

    private final RestUtil restUtil;

    public JwtFilter(RestUtil restUtil) {
        super("JWT", restUtil);
        this.restUtil = restUtil;
    }
}