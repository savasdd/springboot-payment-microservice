package com.payment.gateway.common.filter;

import com.payment.gateway.common.config.UrlPropsConfig;
import com.payment.gateway.common.utils.RestUtil;
import org.springframework.stereotype.Component;

@Component
public class JwtFilter extends FilterFactory {

    private final RestUtil restUtil;
    private final UrlPropsConfig urlConfig;

    public JwtFilter(RestUtil restUtil, UrlPropsConfig urlConfig) {
        super("JWT", restUtil, urlConfig);
        this.restUtil = restUtil;
        this.urlConfig = urlConfig;
    }
}