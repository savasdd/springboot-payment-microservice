package com.payment.stock.service.impl;

import com.google.common.collect.Lists;
import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.config.KafkaTopicsConfig;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.common.utils.CacheUtil;
import com.payment.stock.common.utils.ConstantUtil;
import com.payment.stock.common.utils.RestUtil;
import com.payment.stock.entity.content.KafkaContent;
import com.payment.stock.entity.dto.StockDto;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.entity.model.StockDetail;
import com.payment.stock.entity.vo.StockV0;
import com.payment.stock.repository.CategoryRepository;
import com.payment.stock.repository.StockDetailRepository;
import com.payment.stock.repository.StockRateRepository;
import com.payment.stock.repository.StockRepository;
import com.payment.stock.service.StockService;
import com.payment.stock.service.excel.ExcelUtility;
import com.payment.stock.service.publisher.NotifySerializer;
import com.payment.stock.service.publisher.Publisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StockServiceImpl implements StockService {
    private static final List<String> LANG = List.of("TR", "EN");
    private final StockRepository stockRepository;
    private final StockDetailRepository detailRepository;
    private final StockRateRepository rateRepository;
    private final CategoryRepository categoryRepository;
    private final NotifySerializer notifySerializer;
    private final KafkaTopicsConfig topicsConfig;
    private final Publisher publisher;
    private final BeanUtil beanUtil;
    private final RestUtil restUtil;

    @Cacheable(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, unless = "#result == null || #result.totalCount == 0")
    @Override
    public BaseResponse findAll() {
        List<Stock> stockDtoList = stockRepository.findAllStockByStatusList(RecordStatus.ACTIVE, LANG);

        log.info("find all stock: {}", stockDtoList.size());
        return BaseResponse.success(beanUtil.mapAll(stockDtoList, StockDto.class), (long) stockDtoList.size());
    }

    @Cacheable(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, unless = "#result == null || #result.totalCount == 0")
    @Override
    public BaseResponse findPageable(Pageable pageable) {
        Page<Stock> stockDtoList = stockRepository.findAllStockByStatus(RecordStatus.ACTIVE, LANG, pageable);

        log.info("Pageable all stock: {}", stockDtoList.getTotalElements());
        return BaseResponse.success(beanUtil.mapAll(stockDtoList, StockDto.class), stockDtoList.getTotalElements());
    }

    @Override
    public Page<StockDto> getPageable(Pageable pageable) {
        return beanUtil.mapAll(stockRepository.findByRecordStatus(RecordStatus.ACTIVE, pageable), StockDto.class);
    }

    @Override
    public BaseResponse findAllLoad(DataLoad load) {
        BaseLoadResponse response = stockRepository.load(load);
        List<StockDto> stockDtoList = beanUtil.mapAll(response.getData(), Stock.class, StockDto.class);

        log.info("Load all stock: {}", response.getTotalCount());
        return BaseResponse.success(stockDtoList, response.getTotalCount());
    }

    @Override
    public BaseResponse uploadExcel(Long userId, MultipartFile file) {
        if (!ExcelUtility.hasExcelFormat(file))
            return BaseResponse.error("Excel format not supported");

        try {
            List<StockDto> dtoList = ExcelUtility.excelToDto(userId, file.getInputStream());
            getAsPartition(dtoList).forEach(dto -> stockRepository.saveAll(beanUtil.mapAll(dto, Stock.class)));

            log.info("Upload excel success {}", dtoList.size());
            return BaseResponse.success("Success");
        } catch (Exception e) {
            log.error("Excel upload error", e);
            return BaseResponse.error(e.getMessage());
        }
    }

    private static List<List<StockDto>> getAsPartition(List<StockDto> dtoList) {
        return Lists.partition(dtoList, 50);
    }

    @Cacheable(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, key = "#id", unless = "#result == null || #result.totalCount == 0")
    @Override
    public BaseResponse findById(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        StockDto dto = beanUtil.mapDto(stock, StockDto.class);

        return BaseResponse.success(dto);
    }

    @CacheEvict(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, allEntries = true)
    @Override
    public BaseResponse save(StockV0 dto) {
        Stock stock = beanUtil.mapDto(dto, Stock.class);
        stock.getDetails().forEach(d -> d.setStock(stock));
        stock.setRate(!Objects.isNull(dto.getRate()) ? rateRepository.findById(dto.getRate().getId()).orElseThrow(EntityNotFoundException::new) : null);
        stock.setCategory(!Objects.isNull(dto.getCategory()) ? categoryRepository.findById(dto.getCategory().getId()).orElseThrow(EntityNotFoundException::new) : null);
        Stock model = stockRepository.save(stock);

        log.info("save stock: {}", model);
        publishNotification(dto.getUserId(), ConstantUtil.STOCK_CREATE + " [" + dto.getStockName() + " - " + dto.getAvailableQuantity() + "]");
        return BaseResponse.success(model);
    }

    @CacheEvict(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, allEntries = true)
    @Override
    public BaseResponse update(StockV0 dto) {
        Stock stock = stockRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        //BeanUtils.copyProperties(stock, dto);
        updateField(dto, stock);

        stock.getDetails().forEach(d -> d.setRecordStatus(RecordStatus.DELETED));
        dto.getDetails().forEach(f -> {
            StockDetail detail = detailRepository.findById(f.getId()).orElseThrow(EntityNotFoundException::new);
            BeanUtils.copyProperties(f, detail);
            detail.setStock(stock);
        });

        stock.setRecordStatus(RecordStatus.ACTIVE);
        stockRepository.save(stock);
        log.info("update stock: {}", stock);
        publishNotification(dto.getUserId(), ConstantUtil.STOCK_UPDATE + " [" + dto.getStockName() + " - " + dto.getAvailableQuantity() + "]");
        return BaseResponse.success(stock);
    }

    @CacheEvict(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, allEntries = true)
    @Override
    public BaseResponse delete(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        stock.setRecordStatus(RecordStatus.DELETED);
        Stock model = stockRepository.save(stock);

        log.info("delete stock: {}", model);
        publishNotification(stock.getUserId(), ConstantUtil.STOCK_DELETE + " [" + stock.getStockName() + "]");
        return BaseResponse.success(model);
    }

    @Override
    public BaseResponse updateStockQuantity(Long id, Integer quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        stock.setAvailableQuantity(stock.getAvailableQuantity() > quantity ? stock.getAvailableQuantity() - quantity : 0);
        stockRepository.save(stock);
        log.info("updateStockQuantity: {}", stock);
        return BaseResponse.success(stock);
    }

    private void publishNotification(Long userId, String message) {
        try {
            KafkaContent event = notifySerializer.notification(UUID.randomUUID().toString(), String.valueOf(userId), message);
            log.info("publishing notification event: {}", event);
            publisher.publish(topicsConfig.getTopicName(event.getEventType()), event.getAggregateId(), event);

            log.info("notification event published: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("exception while publishing notification    event: {}", e.getLocalizedMessage());
        }
    }

    private void updateField(StockV0 dto, Stock stock) {
        stock.setUserId(Objects.isNull(dto.getUserId()) ? stock.getUserId() : dto.getUserId());
        stock.setStockName(Objects.isNull(dto.getStockName()) ? stock.getStockName() : dto.getStockName());
        stock.setAvailableQuantity(Objects.isNull(dto.getAvailableQuantity()) ? stock.getAvailableQuantity() : dto.getAvailableQuantity());
        stock.setUnitType(Objects.isNull(dto.getUnitType()) ? stock.getUnitType() : dto.getUnitType());
        stock.setRecordStatus(Objects.isNull(dto.getRecordStatus()) ? stock.getRecordStatus() : dto.getRecordStatus());
        stock.setPrice(Objects.isNull(dto.getPrice()) ? stock.getPrice() : dto.getPrice());
        stock.setYear(Objects.isNull(dto.getYear()) ? stock.getYear() : dto.getYear());
        stock.setRate(!Objects.isNull(dto.getRate()) ? rateRepository.findById(dto.getRate().getId()).orElseThrow(EntityNotFoundException::new) : stock.getRate());
        stock.setCategory(!Objects.isNull(dto.getCategory()) ? categoryRepository.findById(dto.getCategory().getId()).orElseThrow(EntityNotFoundException::new) : stock.getCategory());
    }
}
