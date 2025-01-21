package com.payment.stock.service.impl;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.BasketType;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.entity.base.ValidationDto;
import com.payment.stock.entity.dto.BasketDto;
import com.payment.stock.entity.dto.CommentDto;
import com.payment.stock.entity.model.Basket;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.entity.model.StockDetail;
import com.payment.stock.entity.vo.BasketV0;
import com.payment.stock.entity.vo.StockDetailV0;
import com.payment.stock.entity.vo.StockV0;
import com.payment.stock.repository.BasketRepository;
import com.payment.stock.repository.StockRepository;
import com.payment.stock.service.BasketService;
import com.payment.stock.service.StockService;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse findAll() {
        List<Basket> dtoList = basketRepository.findAll();

        log.info("find all basket: {}", dtoList.size());
        return BaseResponse.success(beanUtil.mapAll(dtoList, CommentDto.class), (long) dtoList.size());
    }

    @Override
    public BaseResponse findAllLoad(DataLoad load, Long userId) {
        BaseLoadResponse response = basketRepository.load(load);
        List<BasketDto> dtoList = beanUtil.mapAll(response.getData(), Basket.class, BasketDto.class);

        log.info("Load all basket: {}", response.getTotalCount());
        return BaseResponse.success(dtoList, response.getTotalCount());
    }


    @Override
    public BaseResponse findById(Long id) {
        Basket model = basketRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return BaseResponse.success(beanUtil.mapDto(model, BasketDto.class));
    }

    @Transactional
    @Override
    public BaseResponse save(BasketV0 dto, Long userId) {
        ValidationDto valid = validation(dto);
        if (valid.isError())
            return BaseResponse.error(valid.getMessage());

        Stock stock = stockRepository.findById(dto.getStock().getId()).orElseThrow(EntityNotFoundException::new);
        if (validateStockQuantity(dto, stock))
            return BaseResponse.error("Yeterli miktarda stok bulunamadı!");

        Basket basket = beanUtil.mapDto(dto, Basket.class);
        basket.setStock(stock);
        basket.setUserId(userId);
        basket.setPrice(stock.getPrice());
        basket.setRate(!Objects.isNull(stock.getRate()) ? stock.getRate().getRate() : new BigDecimal(1));
        basket.setDisprice(getDisCount(basket));
        Basket model = basketRepository.save(basket);

        updateStockQuantity(dto.getQuantity(), stock, false);
        log.info("save basket: {}", model);
        return BaseResponse.success(beanUtil.mapDto(model, BasketDto.class));
    }

    @Async
    @Override
    public void update(List<Long> idList) {
        List<Basket> basketList = basketRepository.findAllByIdInAndRecordStatus(idList, RecordStatus.ACTIVE);
        if (!basketList.isEmpty()) {
            basketList.forEach(f -> f.setType(BasketType.BUY));
        }

        basketRepository.saveAll(basketList);
        log.info("async update basket: {}", idList);
    }


    @Override
    public BaseResponse delete(Long id, Long userId) {
        Optional<Basket> basket = basketRepository.findByIdAndUserIdAndRecordStatus(id, userId, RecordStatus.ACTIVE);

        if (basket.isPresent()) {
            basketRepository.deleteById(id);
            updateStockQuantity(basket.get().getQuantity(), basket.get().getStock(), true);

            log.info("delete basket: {}", id);
            return BaseResponse.success("delete basket");
        } else
            return BaseResponse.error("Kullanıcı Kendi Ürününü Silebilir!");
    }


    private ValidationDto validation(BasketV0 vo) {
        if (Objects.isNull(vo.getStock()))
            return ValidationDto.validation(true, "Stock is required");
        if (Objects.isNull(vo.getQuantity()))
            return ValidationDto.validation(true, "Quantity is required");
        if (vo.getQuantity().equals(0))
            return ValidationDto.validation(true, "Quantity is min 1");

        Optional<Stock> stock = stockRepository.findById(vo.getStock().getId());
        if (stock.isEmpty())
            return ValidationDto.validation(true, "Stock is not found");

        return ValidationDto.validation(false, "Success");

    }

    private void updateStockQuantity(Integer quantity, Stock stock, boolean increase) {
        StockDetail detail = getActiveDetail(stock).findFirst().get();
        StockDetailV0 detailV0 = beanUtil.mapDto(detail, StockDetailV0.class);
        detailV0.setQuantity(increase ? (detail.getQuantity() + quantity) : (detail.getQuantity() - quantity));
        detailV0.setId(null);

        StockV0 v0 = beanUtil.mapDto(stock, StockV0.class);
        v0.setDetails(List.of(detailV0));
        stockService.update(v0);
    }


    private static boolean validateStockQuantity(BasketV0 dto, Stock stock) {
        Integer quantity = getActiveDetail(stock).mapToInt(StockDetail::getQuantity).sum();
        return quantity.equals(0) || dto.getQuantity() > quantity;
    }

    private static Stream<StockDetail> getActiveDetail(Stock stock) {
        return stock.getDetails().stream().filter(f -> f.getRecordStatus().equals(RecordStatus.ACTIVE));
    }

    private static BigDecimal getDisCount(Basket basket) {
        return basket.getPrice().multiply(new BigDecimal(basket.getQuantity())).multiply(basket.getRate());
    }

}
