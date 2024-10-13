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
//            var user = null;
//
//            if ((StringUtils.contains(path, "/all") ||
//                    StringUtils.contains(path, "/getAll") ||
//                    StringUtils.contains(path, "/getOne") ||
//                    StringUtils.contains(path, "/custom")) &&
//                    validate(user.getRoles(), base, ERole.SEARCH.name()))
//                return chain.filter(exchange);
//            else if (StringUtils.contains(path, "/save") && validate(user.getRoles(), base, ERole.CREATE.name()))
//                return chain.filter(exchange);
//            else if (StringUtils.contains(path, "/update") && validate(user.getRoles(), base, ERole.UPDATE.name()))
//                return chain.filter(exchange);
//            else if (StringUtils.contains(path, "/delete") && validate(user.getRoles(), base, ERole.DELETE.name()))
//                return chain.filter(exchange);

            return chain.filter(exchange);

//            var response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
        };

    }


}