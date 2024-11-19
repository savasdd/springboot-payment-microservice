package com.payment.common.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@Getter
@UtilityClass
public class CacheUtil {

    public static final String CACHE_NAME = "payment_cache";
    public static final String CACHE_MANAGER = "cacheManager1Day";
}