package com.payment.stock.service.impl;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.common.utils.RestUtil;
import com.payment.stock.entity.dto.CategoryDto;
import com.payment.stock.entity.model.Category;
import com.payment.stock.repository.CategoryRepository;
import com.payment.stock.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BeanUtil beanUtil;
    private final RestUtil restUtil;

    @Override
    public BaseResponse findAll() {
        List<Category> dtoList = categoryRepository.findAll();

        log.info("find all category: {}", dtoList.size());
        return BaseResponse.success(beanUtil.mapAll(dtoList, CategoryDto.class), (long) dtoList.size());
    }

    @Override
    public BaseResponse findAllLoad(DataLoad load) {
        BaseLoadResponse response = categoryRepository.load(load);
        List<CategoryDto> stockDtoList = beanUtil.mapAll(response.getData(), Category.class, CategoryDto.class);

        log.info("Load all category: {}", response.getTotalCount());
        return BaseResponse.success(stockDtoList, response.getTotalCount());
    }


    @Override
    public BaseResponse findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        CategoryDto dto = beanUtil.mapDto(category, CategoryDto.class);

        return BaseResponse.success(dto);
    }

    @Override
    public BaseResponse save(CategoryDto dto) {
        Category category = beanUtil.mapDto(dto, Category.class);
        Category model = categoryRepository.save(category);

        log.info("save category: {}", model);
        return BaseResponse.success(model);
    }


    @Override
    public BaseResponse update(CategoryDto dto) {
        Category category = categoryRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        updateField(dto, category);

        categoryRepository.save(category);
        log.info("update category: {}", category);
        return BaseResponse.success(category);
    }

    @Override
    public BaseResponse delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        category.setRecordStatus(RecordStatus.DELETED);
        Category model = categoryRepository.save(category);

        log.info("delete category: {}", model);
        return BaseResponse.success(model);
    }

    private void updateField(CategoryDto dto, Category model) {
        model.setCategoryName(Objects.isNull(dto.getCategoryName()) ? model.getCategoryName() : dto.getCategoryName());
        model.setRecordStatus(Objects.isNull(dto.getRecordStatus()) ? model.getRecordStatus() : dto.getRecordStatus());
        model.setDescription(Objects.isNull(dto.getDescription()) ? model.getDescription() : dto.getDescription());
    }

}
