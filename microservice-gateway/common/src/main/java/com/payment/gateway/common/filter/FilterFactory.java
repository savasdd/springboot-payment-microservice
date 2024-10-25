package com.payment.gateway.common.filter;

import com.payment.gateway.common.base.BaseResponse;
import com.payment.gateway.common.config.UrlPropsConfig;
import com.payment.gateway.common.dto.TokenVo;
import com.payment.gateway.common.utils.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.util.Objects;

@Slf4j
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
            serverResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            String authorization = getAuthHeader(request);
            String token = !Objects.isNull(authorization) ? authorization.substring(7, authorization.length()) : null;

            if (Objects.isNull(authorization) || !authorization.startsWith("Bearer ") || Objects.isNull(token)) {
                return serverResponse.setComplete();
            }

            TokenVo tokenVo = new TokenVo();
            tokenVo.setToken(token);
            BaseResponse response = restUtil.exchangePost(getUrl(), tokenVo);

            if (!Objects.isNull(response) && response.getData().equals(true)) {
                log.info("Authorized: {}", response.getData());
                return chain.filter(exchange);
            }

            log.info("Unauthorized: {}", response.getData());
            return serverResponse.setComplete();
        };

    }

    private String getUrl() {
        return urlConfig.getUsers() + "valid-token";
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().containsKey("Authorization") ? request.getHeaders().getOrEmpty("Authorization").get(0) : null;
    }

}