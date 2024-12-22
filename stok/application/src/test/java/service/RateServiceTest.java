package service;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.common.utils.RestUtil;
import com.payment.stock.entity.dto.CategoryDto;
import com.payment.stock.entity.dto.StockRateDto;
import com.payment.stock.entity.model.Category;
import com.payment.stock.entity.model.StockRate;
import com.payment.stock.repository.CategoryRepository;
import com.payment.stock.repository.StockRateRepository;
import com.payment.stock.service.impl.CategoryServiceImpl;
import com.payment.stock.service.impl.RateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RateServiceTest {
    @Mock
    private StockRateRepository repository;
    @Mock
    private BeanUtil beanUtil;
    @InjectMocks
    private RateServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(RateServiceTest.class);
        service = new RateServiceImpl(repository, getBeanUtil());
    }

    @Test
    public void findAll() {
        List<StockRate> list = List.of(new StockRate());
        Mockito.when(repository.findAll()).thenReturn(list);

        BaseResponse response = service.findAll();
        Assertions.assertNotNull(response.getData());
        log.info("Test rate findAll response: {}", response.getTotalCount());
    }

    @Test
    public void findAllLoad() {
        StockRate model = new StockRate();
        model.setRateName("ocak");
        model.setRate(new BigDecimal(35));
        model.setPercent("%" + model.getRate().multiply(new BigDecimal(100)).intValue());
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);

        DataLoad load = new DataLoad();
        load.setTake(10);
        load.setSearchOperation("contains");
        load.setFilter(List.of());
        BaseLoadResponse list = new BaseLoadResponse();
        Mockito.when(repository.load(load)).thenReturn(list);

        BaseResponse response = service.findAllLoad(load);
        Assertions.assertNotNull(response.getData());
        log.info("Test rate findAllLoad response: {}", response.getTotalCount());
    }

    @Test
    public void findById() {
        StockRate model = new StockRate();
        model.setRateName("ocak");
        model.setRate(new BigDecimal(35));
        model.setPercent("%" + model.getRate().multiply(new BigDecimal(100)).intValue());
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        List<StockRate> response = getResponse(service.findById(1L).getData(), StockRate.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getId(), model.getId());

        log.info("Test rate findById response: {}", response.size());
    }


    @Test
    public void save() {
        StockRateDto dto = new StockRateDto();
        dto.setRateName("ocak");
        dto.setRate(new BigDecimal(35));
        dto.setPercent("%" + dto.getRate().multiply(new BigDecimal(100)).intValue());

        StockRate model = new StockRate();
        model.setId(1L);
        model.setRateName(dto.getRateName());
        model.setRate(dto.getRate());
        model.setPercent(dto.getPercent());
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);

        List<StockRate> response = getResponse(List.of(service.save(dto).getData()), StockRate.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getRate(), dto.getRate());
        log.info("Test rate save response: {}", response.size());
    }

    @Test
    public void update() {
        StockRate old = new StockRate();
        old.setRateName("ocak");
        old.setRate(new BigDecimal(35));
        old.setPercent("%" + old.getRate().multiply(new BigDecimal(100)).intValue());
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(old));

        StockRate model = new StockRate();
        model.setId(1L);
        model.setRateName(old.getRateName());
        model.setRate(old.getRate());
        model.setPercent(old.getPercent());
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);

        StockRateDto dto = new StockRateDto();
        dto.setId(1L);
        dto.setRateName("şubat");
        dto.setRate(new BigDecimal(25));

        List<StockRate> response = getResponse(List.of(service.update(dto).getData()), StockRate.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getRate(), dto.getRate());
        log.info("Test rate update response: {}", response.size());
    }

    @Test
    public void delete() {
        StockRate model = new StockRate();
        model.setId(1L);
        model.setRateName("şubat");
        model.setRate(new BigDecimal(25));
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        List<StockRate> response = getResponse(List.of(service.delete(1L).getData()), StockRate.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getRecordStatus(), RecordStatus.DELETED);
        log.info("Test rate delete response: {}", response.size());
    }

    private static BeanUtil getBeanUtil() {
        return new BeanUtil(new ModelMapper());
    }

    private <T> List<T> getResponse(Object data, Class<T> clazz) {
        return getBeanUtil().mapAll(List.of(data), clazz, clazz);
    }
}
