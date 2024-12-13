package com.payment.stock.service.impl;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.common.utils.RestUtil;
import com.payment.stock.entity.dto.StockDto;
import com.payment.stock.entity.dto.StockRateDto;
import com.payment.stock.entity.model.StockRate;
import com.payment.stock.repository.StockRateRepository;
import com.payment.stock.service.RateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RateServiceImpl implements RateService {
    private static final List<String> LANG = List.of("TR", "EN");
    private final StockRateRepository rateRepository;
    private final BeanUtil beanUtil;
    private final RestUtil restUtil;

    @Override
    public BaseResponse findAll() {
        List<StockRate> stockDtoList = rateRepository.findAll();

        log.info("find all rate: {}", stockDtoList.size());
        return BaseResponse.success(beanUtil.mapAll(stockDtoList, StockDto.class), (long) stockDtoList.size());
    }

    @Override
    public BaseResponse findAllLoad(DataLoad load) {
        BaseLoadResponse response = rateRepository.load(load);
        List<StockRateDto> stockDtoList = beanUtil.mapAll(response.getData(), StockRate.class, StockRateDto.class);

        log.info("Load all rate: {}", response.getTotalCount());
        return BaseResponse.success(stockDtoList, response.getTotalCount());
    }


    @Override
    public BaseResponse findById(Long id) {
        StockRate rate = rateRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        StockDto dto = beanUtil.mapDto(rate, StockDto.class);

        return BaseResponse.success(dto);
    }

    @Override
    public BaseResponse save(StockRateDto dto) {
        StockRate rate = beanUtil.mapDto(dto, StockRate.class);
        rate.setPercent(getPercent(rate.getRate()));
        StockRate model = rateRepository.save(rate);

        log.info("save: {}", model);
        return BaseResponse.success(model);
    }


    @Override
    public BaseResponse update(StockRateDto dto) {
        StockRate rate = rateRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        updateField(dto, rate);

        rateRepository.save(rate);
        log.info("update rate: {}", rate);
        return BaseResponse.success(rate);
    }

    @Override
    public BaseResponse delete(Long id) {
        StockRate rate = rateRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        rate.setRecordStatus(RecordStatus.DELETED);
        StockRate model = rateRepository.save(rate);

        log.info("delete rate: {}", model);
        return BaseResponse.success(model);
    }

    private void updateField(StockRateDto dto, StockRate model) {
        model.setRateName(Objects.isNull(dto.getRateName()) ? model.getRateName() : dto.getRateName());
        model.setRate(Objects.isNull(dto.getRate()) ? model.getRate() : dto.getRate());
        model.setRecordStatus(Objects.isNull(dto.getRecordStatus()) ? model.getRecordStatus() : dto.getRecordStatus());
        model.setPercent(getPercent(Objects.isNull(dto.getRate()) ? model.getRate() : dto.getRate()));
    }

    private static String getPercent(BigDecimal rate) {
        return rate.multiply(new BigDecimal(100)).intValue() + "/100";
    }
}
