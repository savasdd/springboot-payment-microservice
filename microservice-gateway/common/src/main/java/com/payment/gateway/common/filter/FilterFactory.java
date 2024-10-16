package com.payment.gateway.common.filter;

import com.payment.gateway.common.base.BaseResponse;
import com.payment.gateway.common.config.UrlPropsConfig;
import com.payment.gateway.common.dto.TokenVo;
import com.payment.gateway.common.utils.RestUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.util.Objects;

public abstract class FilterFactory extends AbstractGatewayFilterFactory {
    private final RestUtil restUtil;
    private final UrlPropsConfig urlConfig;

    public FilterFactory(String filter, RestUtil restUtil, UrlPropsConfig urlConfig) {
        this.restUtil = restUtil;
        this.urlConfig = urlConfig;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse serverResponse = exchange.getResponse();
            String path = request.getPath().toString();
            String authorization = request.getHeaders().containsKey("Authorization") ? request.getHeaders().get("Authorization").toString() : null;
            String token = !Objects.isNull(authorization) ? authorization.substring(7, authorization.length()) : null;

            if (Objects.isNull(authorization) || !authorization.startsWith("Bearer ") || Objects.isNull(token))
                serverResponse.setStatusCode(HttpStatus.UNAUTHORIZED);

            TokenVo tokenVo = new TokenVo();
            tokenVo.setToken(token);
            BaseResponse response = restUtil.exchangePost(getUrl(), tokenVo);

            if (!Objects.isNull(response) && response.getData().equals(true))
                return chain.filter(exchange);

            return serverResponse.setComplete();
        };

    }

    private String getUrl() {
        return urlConfig.getUsers() + "valid-token";
    }

}