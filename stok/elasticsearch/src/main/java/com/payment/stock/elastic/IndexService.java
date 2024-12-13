package com.payment.stock.elastic;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;

public interface IndexService {
    BaseResponse index(DataLoad load);
}
