package com.payment.stock.service;

import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.entity.dto.StockDto;
import com.payment.stock.entity.vo.StockV0;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StockService {
    BaseResponse findAll();

    BaseResponse findById(Long id);

    BaseResponse save(StockV0 dto);

    BaseResponse update(StockV0 dto);

    BaseResponse delete(Long id);

    BaseResponse updateStockQuantity(Long id, Integer quantity);

    BaseResponse findPageable(Pageable pageable);

    Page<StockDto> getPageable(Pageable pageable);

    BaseResponse findAllLoad(DataLoad load);

    BaseResponse uploadExcel(Long userId, MultipartFile file);
}
