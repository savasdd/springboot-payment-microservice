package service;

import com.load.base.BaseLoadResponse;
import com.payment.stock.application.StockApplication;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.entity.dto.StockRateDto;
import com.payment.stock.entity.model.StockRate;
import com.payment.stock.repository.StockRateRepository;
import com.payment.stock.service.impl.RateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import util.TestUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(classes = StockApplication.class)
public class RateServiceTest {
    @Mock
    private StockRateRepository repository;
    @Autowired
    private BeanUtil beanUtil;
    @InjectMocks
    private RateServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        service = new RateServiceImpl(repository, beanUtil);
    }

    @Test
    public void findAll() {
        List<StockRate> list = List.of(getRate());
        Mockito.when(repository.findAll()).thenReturn(list);

        BaseResponse response = service.findAll();
        Assertions.assertNotNull(response.getData());
        log.info("Test rate findAll success");
    }

    @Test
    public void findAllLoad() {
        StockRate model = getRate();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);

        BaseLoadResponse list = new BaseLoadResponse();
        Mockito.when(repository.load(TestUtil.getDataLoad())).thenReturn(list);

        BaseResponse response = service.findAllLoad(TestUtil.getDataLoad());
        Assertions.assertNotNull(response.getData());
        log.info("Test rate findAllLoad success");
    }


    @Test
    public void findById() {
        StockRate model = getRate();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        List<StockRate> response = TestUtil.getResponse(beanUtil, service.findById(1L).getData(), StockRate.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getId(), model.getId());
        log.info("Test rate findById success");
    }


    @Test
    public void save() {
        StockRateDto dto = new StockRateDto();
        dto.setRateName("ocak");
        dto.setRate(new BigDecimal(35));
        dto.setPercent("%" + dto.getRate().multiply(new BigDecimal(100)).intValue());

        StockRate model = beanUtil.mapDto(dto, StockRate.class);
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);

        List<StockRate> response = TestUtil.getResponse(beanUtil, service.save(dto).getData(), StockRate.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getRate(), dto.getRate());
        log.info("Test rate save success");
    }

    @Test
    public void update() {
        StockRate model = getRate();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        StockRateDto dto = new StockRateDto();
        dto.setId(1L);
        dto.setRateName("şubat");
        dto.setRate(new BigDecimal(25));

        List<StockRate> response = TestUtil.getResponse(beanUtil, service.update(dto).getData(), StockRate.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getRate(), dto.getRate());
        log.info("Test rate update success");
    }

    @Test
    public void delete() {
        StockRate model = getRate();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        List<StockRate> response = TestUtil.getResponse(beanUtil, service.delete(1L).getData(), StockRate.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getRecordStatus(), RecordStatus.DELETED);
        log.info("Test rate delete success");
    }


    private static StockRate getRate() {
        StockRate model = new StockRate();
        model.setRateName("ocak");
        model.setRate(new BigDecimal(35));
        model.setPercent("%" + model.getRate().multiply(new BigDecimal(100)).intValue());
        model.setCreDate(new Date());
        model.setRecordStatus(RecordStatus.ACTIVE);
        return model;
    }
}
