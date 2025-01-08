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
import com.payment.stock.entity.model.Category;
import com.payment.stock.entity.model.Property;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.entity.model.StockDetail;
import com.payment.stock.entity.vo.StockV0;
import com.payment.stock.repository.*;
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
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StockServiceImpl implements StockService {
    private static final List<String> LANG = List.of("TR", "EN");
    private final StockRepository stockRepository;
    private final StockRateRepository rateRepository;
    private final CategoryRepository categoryRepository;
    private final PropertyRepository propertyRepository;
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
        stock.setCategoryList(getCategory(dto, stock));
        stock.setPropertyList(getProperties(dto, stock));
        stock.setRate(!Objects.isNull(dto.getRate()) ? rateRepository.findById(dto.getRate().getId()).orElseThrow(EntityNotFoundException::new) : null);
        Stock model = stockRepository.saveAndFlush(stock);

        log.info("save stock: {}", model);
        publishNotification(dto.getUserId(), ConstantUtil.STOCK_CREATE + " [" + dto.getStockName() + " - " + dto.getPrice() + "]");
        return BaseResponse.success(beanUtil.mapDto(model, StockDto.class));
    }


    @CacheEvict(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, allEntries = true)
    @Override
    public BaseResponse update(StockV0 dto) {
        Stock stock = stockRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        //BeanUtils.copyProperties(stock, dto);
        updateField(dto, stock);
        stockRepository.save(stock);
        log.info("update stock: {}", stock);
        publishNotification(dto.getUserId(), ConstantUtil.STOCK_UPDATE + " [" + dto.getStockName() + " - " + dto.getPrice() + "]");
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
        List<Property> propertyList = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();
        List<StockDetail> detailList = new ArrayList<>();

        stock.setUserId(Objects.isNull(dto.getUserId()) ? stock.getUserId() : dto.getUserId());
        stock.setStockName(Objects.isNull(dto.getStockName()) ? stock.getStockName() : dto.getStockName());
        stock.setRecordStatus(Objects.isNull(dto.getRecordStatus()) ? stock.getRecordStatus() : dto.getRecordStatus());
        stock.setPrice(Objects.isNull(dto.getPrice()) ? stock.getPrice() : dto.getPrice());
        stock.setRate(!Objects.isNull(dto.getRate()) ? rateRepository.findById(dto.getRate().getId()).orElseThrow(EntityNotFoundException::new) : stock.getRate());

        dto.getPropertyList().forEach(f -> {
            Property property = propertyRepository.findById(f.getId()).orElseThrow(EntityNotFoundException::new);
            BeanUtils.copyProperties(f, property);
            property.setStockList(List.of(stock));
            propertyList.add(property);
        });
        dto.getCategoryList().forEach(f -> {
            Category category = categoryRepository.findById(f.getId()).orElseThrow(EntityNotFoundException::new);
            BeanUtils.copyProperties(f, category);
            category.setStockList(List.of(stock));
            categoryList.add(category);
        });

        dto.getDetails().forEach(f -> {
            StockDetail detail = new StockDetail(f.getLanguage(), f.getQuantity(), f.getUnitType(), stock);
            BeanUtils.copyProperties(f, detail);
            detailList.add(detail);
        });
        if (!detailList.isEmpty())
            stock.getDetails().forEach(d -> d.setRecordStatus(RecordStatus.DELETED));

        stock.setPropertyList(propertyList.isEmpty() ? stock.getPropertyList() : propertyList);
        stock.setCategoryList(categoryList.isEmpty() ? stock.getCategoryList() : categoryList);
        stock.setDetails(detailList.isEmpty() ? stock.getDetails() : detailList);
    }

    private List<Category> getCategory(StockV0 dto, Stock stock) {
        List<Category> categoryList = dto.getCategoryList().stream().map(m -> categoryRepository.findById(m.getId()).orElse(null)).toList();
        categoryList.forEach(f -> f.setStockList(List.of(stock)));
        return categoryList;
    }

    private List<Property> getProperties(StockV0 dto, Stock stock) {
        List<Property> propertyList = dto.getPropertyList().stream().map(m -> propertyRepository.findById(m.getId()).orElse(null)).toList();
        propertyList.forEach(f -> f.setStockList(List.of(stock)));
        return propertyList;
    }
}
