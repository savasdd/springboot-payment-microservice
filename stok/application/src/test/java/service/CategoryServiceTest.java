package service;

import com.load.base.BaseLoadResponse;
import com.payment.stock.application.StockApplication;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.common.utils.RestUtil;
import com.payment.stock.entity.dto.CategoryDto;
import com.payment.stock.entity.model.Category;
import com.payment.stock.repository.CategoryRepository;
import com.payment.stock.service.impl.CategoryServiceImpl;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest(classes = StockApplication.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository repository;
    @Autowired
    private BeanUtil beanUtil;
    @Autowired
    private RestUtil restUtil;
    @InjectMocks
    private CategoryServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        service = new CategoryServiceImpl(repository, beanUtil, restUtil);
    }

    @Test
    public void findAll() {
        List<Category> list = List.of(getCategory());
        Mockito.when(repository.findAll()).thenReturn(list);

        BaseResponse response = service.findAll();
        Assertions.assertNotNull(response.getData());
        log.info("Test category findAll success");
    }

    @Test
    public void findAllLoad() {
        List<Category> list = List.of(getCategory());
        Mockito.when(repository.findAll()).thenReturn(list);
        BaseLoadResponse baseLoadResponse = new BaseLoadResponse();
        Mockito.when(repository.load(TestUtil.getDataLoad())).thenReturn(baseLoadResponse);

        BaseResponse response = service.findAllLoad(TestUtil.getDataLoad());
        Assertions.assertNotNull(response.getData());
        log.info("Test category findAllLoad success");
    }

    @Test
    public void findById() {
        Category model = getCategory();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        List<Category> response = TestUtil.getResponse(beanUtil, service.findById(1L).getData(), Category.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getId(), model.getId());
        log.info("Test category findById success");
    }


    @Test
    public void save() {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryName("inşaat");
        dto.setDescription("inşaat malzemesi");

        Category model = beanUtil.mapDto(dto, Category.class);
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);

        List<Category> response = TestUtil.getResponse(beanUtil, service.save(dto).getData(), Category.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getCategoryName(), dto.getCategoryName());
        log.info("Test save success");
    }

    @Test
    public void update() {
        Category model = getCategory();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setCategoryName("Mobilya");

        List<Category> response = TestUtil.getResponse(beanUtil, service.update(dto).getData(), Category.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getCategoryName(), dto.getCategoryName());
        log.info("Test update success");
    }

    @Test
    public void delete() {
        Category model = getCategory();
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        List<Category> response = TestUtil.getResponse(beanUtil, service.delete(1L).getData(), Category.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getRecordStatus(), RecordStatus.DELETED);
        log.info("Test delete success");
    }

    private static Category getCategory() {
        Category model = new Category();
        model.setId(1L);
        model.setCategoryName("inşaat");
        model.setDescription("inşaat malzemesi");
        model.setRecordStatus(RecordStatus.ACTIVE);
        model.setCreDate(new Date());
        return model;
    }

}
