package com.payment.gateway.common.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

public abstract class FilterFactory extends AbstractGatewayFilterFactory {

    private String base;

    public FilterFactory(String base) {
        this.base = base;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            String path = request.getPath().toString();
            var authorization = request.getHeaders().containsKey("Authorization") ? request.getHeaders().get("Authorization").toString() : null;
            var token = authorization.substring(7, authorization.length());



            return chain.filter(exchange);

//            var response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
        };

    }


}