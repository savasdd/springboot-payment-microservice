package com.payment.stock.service.impl;

import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.common.utils.CacheUtil;
import com.payment.stock.common.utils.ConstantUtil;
import com.payment.stock.common.utils.RestUtil;
import com.payment.stock.entity.dto.StockDto;
import com.payment.stock.entity.dto.UserDto;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.repository.StockRepository;
import com.payment.stock.service.StockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    private final RestUtil restUtil;

    @Cacheable(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, unless = "#result == null || #result.count == 0")
    @Override
    public BaseResponse findAll(Pageable pageable) {
        List<Stock> stockDtoList = stockRepository.findByRecordStatus(RecordStatus.ACTIVE, pageable).getContent();

        log.info("find all stock: {}", stockDtoList.size());
        return BaseResponse.builder().data(beanUtil.mapAll(stockDtoList, StockDto.class)).count(stockDtoList.size()).build();
    }

    @Cacheable(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, key = "#id", unless = "#result == null || #result.count == 0")
    @Override
    public BaseResponse findById(Long id) {
        return BaseResponse.builder().data(stockRepository.findById(id).orElseThrow(EntityNotFoundException::new)).build();
    }

    @CacheEvict(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, allEntries = true)
    @Override
    public BaseResponse save(StockDto dto) {
        UserDto userDto = restUtil.exchangeGet(ConstantUtil.USER_URL + "findOne/" + dto.getUserId(), UserDto.class);

        if (userDto == null)
            throw new RuntimeException("user not found");

        Stock stock = beanUtil.mapDto(dto, Stock.class);
        Stock model = stockRepository.save(stock);

        log.info("save: {}", model);
        return BaseResponse.builder().data(model).build();
    }

    @CacheEvict(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, allEntries = true)
    @Override
    public BaseResponse update(Long id, StockDto dto) {
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Stock update = beanUtil.transform(dto, stock);
        Stock model = stockRepository.save(update);

        log.info("update: {}", model);
        return BaseResponse.builder().data(model).build();
    }

    @CacheEvict(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, allEntries = true)
    @Override
    public BaseResponse delete(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        stock.setRecordStatus(RecordStatus.DELETED);
        Stock model = stockRepository.save(stock);

        log.info("delete: {}", model);
        return BaseResponse.builder().data(model).build();
    }

    @Override
    public BaseResponse updateStockQuantity(Long id, Integer quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        stock.setAvailableQuantity(stock.getAvailableQuantity() > quantity ? stock.getAvailableQuantity() - quantity : 0);
        stockRepository.save(stock);
        log.info("updateStockQuantity: {}", stock);
        return BaseResponse.builder().data(stock).build();
    }

    @Override
    public List<StockDto> findAllList(Pageable pageable) {
        return beanUtil.mapAll(stockRepository.findAll(pageable).getContent(), StockDto.class);
    }
}
