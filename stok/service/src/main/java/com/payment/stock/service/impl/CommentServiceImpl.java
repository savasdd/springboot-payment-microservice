package com.payment.stock.service.impl;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.entity.base.ValidationDto;
import com.payment.stock.entity.dto.CommentDto;
import com.payment.stock.entity.model.Comment;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.entity.vo.CommentV0;
import com.payment.stock.repository.CommentRepository;
import com.payment.stock.repository.StockRepository;
import com.payment.stock.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final StockRepository stockRepository;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse findAll() {
        List<Comment> dtoList = commentRepository.findAll();

        log.info("find all comment: {}", dtoList.size());
        return BaseResponse.success(beanUtil.mapAll(dtoList, CommentDto.class), (long) dtoList.size());
    }

    @Override
    public BaseResponse findAllLoad(DataLoad load) {
        BaseLoadResponse response = commentRepository.load(load);
        List<CommentDto> stockDtoList = beanUtil.mapAll(response.getData(), Comment.class, CommentDto.class);

        log.info("Load all comment: {}", response.getTotalCount());
        return BaseResponse.success(stockDtoList, response.getTotalCount());
    }


    @Override
    public BaseResponse findById(Long id) {
        Comment model = commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return BaseResponse.success(beanUtil.mapDto(model, CommentDto.class));
    }

    @Override
    public BaseResponse save(CommentV0 dto) {
        ValidationDto valid = validation(dto);
        if (valid.isError())
            return BaseResponse.error(valid.getMessage());

        Comment comment = beanUtil.mapDto(dto, Comment.class);
        comment.setStock(stockRepository.findById(dto.getStock().getId()).orElseThrow(EntityNotFoundException::new));
        Comment model = commentRepository.save(comment);

        log.info("save comment: {}", model);
        return BaseResponse.success(beanUtil.mapDto(model, CommentDto.class));
    }


    @Override
    public BaseResponse update(CommentV0 dto) {
        ValidationDto valid = validation(dto);
        if (valid.isError())
            return BaseResponse.error(valid.getMessage());

        Comment comment = commentRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        updateField(dto, comment);

        commentRepository.save(comment);
        log.info("update comment: {}", comment);
        return BaseResponse.success(beanUtil.mapDto(comment, CommentDto.class));
    }

    @Override
    public BaseResponse delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        comment.setRecordStatus(RecordStatus.DELETED);
        Comment model = commentRepository.save(comment);

        log.info("delete comment: {}", model);
        return BaseResponse.success(model);
    }

    private void updateField(CommentV0 dto, Comment model) {
        model.setComment(Objects.isNull(dto.getComment()) ? model.getComment() : dto.getComment());
        model.setLanguage(Objects.isNull(dto.getLanguage()) ? model.getLanguage() : dto.getLanguage());
        model.setRecordStatus(Objects.isNull(dto.getRecordStatus()) ? model.getRecordStatus() : dto.getRecordStatus());
        model.setStock(model.getStock());
    }


    private ValidationDto validation(CommentV0 vo) {
        if (Objects.isNull(vo.getStock()))
            return ValidationDto.validation(true, "Stock is required");
        if (Objects.isNull(vo.getComment()))
            return ValidationDto.validation(true, "Comment is required");

        Optional<Stock> stock = stockRepository.findById(vo.getStock().getId());
        if (stock.isEmpty())
            return ValidationDto.validation(true, "Stock is not found");

        return ValidationDto.validation(false, "Success");

    }

}
