package com.payment.stock.common.utils;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class HeaderUtil {
    private static final Long defaultUserId = 11L;
    private static final String USER_ID = "user-id";
    private static final String USER_FIRSTNAME = "user-first-name";
    private static final String USER_LASTNAME = "user-last-name";

    public static Long getUserId(HttpServletRequest request) {
        Map<String, Serializable> headers = extracted(request);
        return headers.containsKey(USER_ID) ? Long.valueOf(headers.get(USER_ID).toString()) : defaultUserId;
    }

    public static String getFullName(HttpServletRequest request) {
        Map<String, Serializable> headers = extracted(request);
        String firstName = headers.containsKey(USER_FIRSTNAME) ? (String) headers.get(USER_FIRSTNAME) : "";
        String lastName = headers.containsKey(USER_LASTNAME) ? (String) headers.get(USER_LASTNAME) : "";
        return firstName.concat(" ").concat(lastName);
    }


    private static Map<String, Serializable> extracted(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream().collect(Collectors.toMap(h -> h, h -> {
            ArrayList<String> headerValues = Collections.list(request.getHeaders(h));
            return headerValues.size() == 1 ? headerValues.get(0) : headerValues;
        }));
    }
}
