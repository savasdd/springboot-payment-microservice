package com.payment.stock.common.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@Getter
@UtilityClass
public class CacheUtil {

    public static final String CACHE_NAME = "payment_stock_cache";
    public static final String GET_IMAGE = "stock_image_cache";
    public static final String CACHE_MANAGER = "cacheManager1Day";
}
