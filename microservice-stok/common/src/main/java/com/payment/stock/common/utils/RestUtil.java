package com.payment.stock.common.utils;

import com.payment.stock.common.base.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RestUtil {

    private final RestTemplate restTemplate;
    private final BeanUtil beanUtil;


    public <T> T exchangeGet(String uri, Class<T> dto) {
        ResponseEntity<BaseResponse> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(getHeaders()), BaseResponse.class);
        T resp = beanUtil.mapDto(Objects.requireNonNull(response.getBody()).getData(), dto);
        return response.getStatusCode().value() == 200 ? resp : null;
    }

    public <T> List<T> exchangeAsList(String uri, ParameterizedTypeReference<List<T>> responseType) {
        return restTemplate.exchange(uri, HttpMethod.GET, null, responseType).getBody();
    }


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        return headers;
    }
}
