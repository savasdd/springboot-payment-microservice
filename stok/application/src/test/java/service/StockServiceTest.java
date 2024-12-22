package service;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.load.options.SortOptions;
import com.payment.stock.application.StockApplication;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.config.KafkaTopicsConfig;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.enums.UnitType;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.common.utils.DateUtil;
import com.payment.stock.common.utils.RestUtil;
import com.payment.stock.entity.dto.CategoryDto;
import com.payment.stock.entity.dto.StockDetailDto;
import com.payment.stock.entity.dto.StockRateDto;
import com.payment.stock.entity.model.Category;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.entity.model.StockDetail;
import com.payment.stock.entity.model.StockRate;
import com.payment.stock.entity.vo.CategoryV0;
import com.payment.stock.entity.vo.StockRateV0;
import com.payment.stock.entity.vo.StockV0;
import com.payment.stock.repository.CategoryRepository;
import com.payment.stock.repository.StockDetailRepository;
import com.payment.stock.repository.StockRateRepository;
import com.payment.stock.repository.StockRepository;
import com.payment.stock.service.impl.RateServiceImpl;
import com.payment.stock.service.impl.StockServiceImpl;
import com.payment.stock.service.publisher.NotifySerializer;
import com.payment.stock.service.publisher.Publisher;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import util.TestUtil;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(classes = StockApplication.class)
public class StockServiceTest {
    private static final List<String> LANG = List.of("TR", "EN");
    @Mock
    private StockRepository repository;
    @Mock
    private StockDetailRepository detailRepository;
    @Mock
    private StockRateRepository rateRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Autowired
    private NotifySerializer notifySerializer;
    @Autowired
    private KafkaTopicsConfig topicsConfig;
    @Mock
    private Publisher publisher;
    @Autowired
    private BeanUtil beanUtil;
    @Autowired
    private RestUtil restUtil;
    @InjectMocks
    private StockServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        service = new StockServiceImpl(repository, detailRepository, rateRepository, categoryRepository, notifySerializer, topicsConfig, publisher, beanUtil, restUtil);
    }

    @Test
    public void findAll() {
        List<Stock> list = List.of(getStock());
        Mockito.when(repository.findAll()).thenReturn(list);

        BaseResponse response = service.findAll();
        Assertions.assertNotNull(response.getData());
        log.info("Test stock findAll success");
    }

    @Test
    public void findPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        Stock model = getStock();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);

        Page<Stock> pageList = new PageImpl<>(List.of(model));
        Mockito.when(repository.findAllStockByStatus(RecordStatus.ACTIVE, LANG, pageable)).thenReturn(pageList);

        BaseResponse response = service.findPageable(pageable);
        Assertions.assertNotNull(response.getData());
        log.info("Test stock findPageable success");
    }

    @Test
    public void findAllLoad() {
        Stock model = getStock();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);

        BaseLoadResponse list = new BaseLoadResponse();
        Mockito.when(repository.load(TestUtil.getDataLoad())).thenReturn(list);

        BaseResponse response = service.findAllLoad(TestUtil.getDataLoad());
        Assertions.assertNotNull(response.getData());
        log.info("Test stock findAllLoad success");
    }


    @Test
    public void findById() {
        Stock model = getStock();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        List<Stock> response = TestUtil.getResponse(beanUtil, service.findById(1L).getData(), Stock.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getId(), model.getId());
        log.info("Test stock findById success");
    }


    @Test
    public void save() {
        StockV0 dto = new StockV0();
        dto.setId(1L);
        dto.setStockName("demir");
        dto.setYear(DateUtil.getYear(new Date()));
        dto.setPrice(new BigDecimal("100.00"));
        dto.setUnitType(UnitType.Ton);
        dto.setAvailableQuantity(500);
        dto.setCategory(new CategoryV0(getCategory().getId()));
        dto.setRate(new StockRateV0(getStockRate().getId()));
        dto.setDetails(beanUtil.mapAll(getStockDetails(), StockDetailDto.class));
        dto.setCreDate(new Date());
        dto.setRecordStatus(RecordStatus.ACTIVE);

        Stock model = beanUtil.mapDto(dto, Stock.class);
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(getCategory()));
        Mockito.when(rateRepository.findById(1L)).thenReturn(Optional.of(getStockRate()));

        List<Stock> response = TestUtil.getResponse(beanUtil, service.save(dto).getData(), Stock.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getPrice(), dto.getPrice());
        log.info("Test stock save success");
    }

    @Test
    public void update() {
        Stock model = getStock();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(getCategory()));
        Mockito.when(rateRepository.findById(1L)).thenReturn(Optional.of(getStockRate()));

        StockDetail detail = getStockDetails().stream().findFirst().orElseThrow();
        detail.setStock(model);
        Mockito.when(detailRepository.findById(1L)).thenReturn(Optional.of(detail));

        StockV0 dto = new StockV0();
        dto.setId(1L);
        dto.setStockName("kazma");
        dto.setPrice(new BigDecimal("200.00"));
        dto.setUnitType(UnitType.Adet);
        dto.setDetails(beanUtil.mapAll(getStockDetails(), StockDetailDto.class));

        List<Stock> response = TestUtil.getResponse(beanUtil, service.update(dto).getData(), Stock.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getStockName(), dto.getStockName());
        log.info("Test stock update success");
    }

    @Test
    public void delete() {
        Stock model = getStock();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        List<Stock> response = TestUtil.getResponse(beanUtil, service.delete(1L).getData(), Stock.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getRecordStatus(), RecordStatus.DELETED);
        log.info("Test stock delete success");
    }

    private Stock getStock() {
        Stock model = new Stock();
        model.setId(1L);
        model.setStockName("demir");
        model.setYear(DateUtil.getYear(new Date()));
        model.setPrice(new BigDecimal("100.00"));
        model.setUnitType(UnitType.Ton);
        model.setAvailableQuantity(500);
        model.setCategory(getCategory());
        model.setRate(getStockRate());
        model.setDetails(getStockDetails());
        model.setCreDate(new Date());
        model.setRecordStatus(RecordStatus.ACTIVE);
        return model;
    }

    private Category getCategory() {
        Category model = new Category();
        model.setId(1L);
        model.setCategoryName("inşaat");
        return model;
    }

    private StockRate getStockRate() {
        StockRate model = new StockRate();
        model.setId(1L);
        model.setRateName("yil sonu");
        return model;
    }

    private List<StockDetail> getStockDetails() {
        StockDetail model = new StockDetail();
        model.setId(1L);
        model.setLanguage("TR");
        model.setTitle("malzeme");
        model.setCreatedDate(new Date());
        model.setRecordStatus(RecordStatus.ACTIVE);
        return List.of(model);
    }

}
