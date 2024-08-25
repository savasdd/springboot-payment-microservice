package com.payment.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstantUtil {
    public static final String STOCK_URL = "http://localhost:8086/api/payment/stocks/";

    public static final String ORDER_SUCCESS = "Sipariş Başarılıyla Oluşturuldu";
    public static final String PRODUCT_ADD = "Ürün Başarılıyla Eklendi";
    public static final String PRODUCT_REMOVE = "Ürün Başarılıyla Silindi";
    public static final String ORDER_PAYMENT = "Ödeme Başarıyla Alınmıştır";
    public static final String ORDER_CANSEL = "Ödeme Başarıyla İptal Edilmiştir";
    public static final String ORDER_SUBMIT = "Ödeme Başarıyla Kabul Edilmiştir";
    public static final String ORDER_COMPETE = "Ödeme Başarıyla Tamamlanmıştır";
}
