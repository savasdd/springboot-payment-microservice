package com.payment.stock.service.impl;

import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.entity.dto.StockDto;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.repository.StockRepository;
import com.payment.stock.service.StockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse findAll(Pageable pageable) {
        List<Stock> stockDtoList = stockRepository.findByRecordStatus(RecordStatus.ACTIVE, pageable).getContent();

        log.info("find all stock: {}", stockDtoList.size());
        return BaseResponse.builder().data(beanUtil.mapAll(stockDtoList, StockDto.class)).count(stockDtoList.size()).build();
    }

    @Override
    public BaseResponse findById(Long id) {
        return BaseResponse.builder().data(stockRepository.findById(id).orElseThrow(EntityNotFoundException::new)).build();
    }

    @Override
    public BaseResponse save(StockDto dto) {
        Stock stock = beanUtil.mapDto(dto, Stock.class);
        Stock model = stockRepository.save(stock);

        log.info("save: {}", model);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse update(Long id, StockDto dto) {
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Stock update = beanUtil.transform(dto, stock);
        Stock model = stockRepository.save(update);

        log.info("update: {}", model);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse delete(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        stock.setRecordStatus(RecordStatus.DELETED);
        Stock model = stockRepository.save(stock);

        log.info("delete: {}", model);
        return BaseResponse.builder().data(model).build();
    }
}
