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
        List<Category> list = List.of(new Category(), new Category());
        Mockito.when(repository.findAll()).thenReturn(list);

        BaseResponse response = service.findAll();
        Assertions.assertNotNull(response.getData());
        log.info("Test findAll response: {}", response.getTotalCount());
    }

    @Test
    public void findAllLoad() {
        BaseLoadResponse list = new BaseLoadResponse();
        Mockito.when(repository.load(TestUtil.getDataLoad())).thenReturn(list);

        BaseResponse response = service.findAllLoad(TestUtil.getDataLoad());
        Assertions.assertNotNull(response.getData());
        log.info("Test findAllLoad response: {}", response.getTotalCount());
    }

    @Test
    public void findById() {
        Category model = new Category();
        model.setCategoryName("inşaat");
        model.setDescription("inşaat malzemesi");
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        List<Category> response = TestUtil.getResponse(beanUtil, service.findById(1L).getData(), Category.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getId(), model.getId());

        log.info("Test findById response: {}", response.size());
    }

    @Test
    public void save() {
        CategoryDto dto = new CategoryDto();
        dto.setCategoryName("inşaat");
        dto.setDescription("inşaat malzemesi");

        Category model = new Category();
        model.setId(1L);
        model.setCategoryName(dto.getCategoryName());
        model.setDescription(dto.getDescription());
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);

        List<Category> response = TestUtil.getResponse(beanUtil, service.save(dto).getData(), Category.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getCategoryName(), dto.getCategoryName());
        log.info("Test save success");
    }

    @Test
    public void update() {
        Category old = new Category();
        old.setCategoryName("inşaat");
        old.setDescription("inşaat malzemesi");
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(old));

        Category model = new Category();
        model.setId(1L);
        model.setCategoryName(old.getCategoryName());
        model.setDescription(old.getDescription());
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);

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
        Category model = new Category();
        model.setId(1L);
        model.setCategoryName("inşaat");
        Mockito.when(repository.save(Mockito.any())).thenReturn(model);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(model));

        List<Category> response = TestUtil.getResponse(beanUtil, service.delete(1L).getData(), Category.class);
        Assertions.assertNotEquals(response.size(), 0);
        Assertions.assertEquals(response.stream().findFirst().orElseThrow().getRecordStatus(), RecordStatus.DELETED);
        log.info("Test delete success");
    }


}
