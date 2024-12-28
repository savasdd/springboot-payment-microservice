package com.payment.stock.service.impl;

import com.load.base.BaseLoadResponse;
import com.load.impl.DataLoad;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.enums.ActionType;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.entity.base.ValidationDto;
import com.payment.stock.entity.dto.ActionsDto;
import com.payment.stock.entity.model.Actions;
import com.payment.stock.entity.model.Comment;
import com.payment.stock.entity.vo.ActionsV0;
import com.payment.stock.repository.ActionsRepository;
import com.payment.stock.repository.CommentRepository;
import com.payment.stock.service.ActionsService;
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
public class ActionsServiceImpl implements ActionsService {
    private final CommentRepository commentRepository;
    private final ActionsRepository actionsRepository;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse findAll() {
        List<Actions> dtoList = actionsRepository.findAll();

        log.info("find all actions: {}", dtoList.size());
        return BaseResponse.success(beanUtil.mapAll(dtoList, ActionsDto.class), (long) dtoList.size());
    }

    @Override
    public BaseResponse findAllLoad(DataLoad load) {
        BaseLoadResponse response = actionsRepository.load(load);
        List<ActionsDto> stockDtoList = beanUtil.mapAll(response.getData(), Actions.class, ActionsDto.class);

        log.info("Load all actions: {}", response.getTotalCount());
        return BaseResponse.success(stockDtoList, response.getTotalCount());
    }


    @Override
    public BaseResponse findById(Long id) {
        Actions model = actionsRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return BaseResponse.success(beanUtil.mapDto(model, ActionsDto.class));
    }

    @Override
    public BaseResponse save(ActionsV0 dto) {
        ValidationDto valid = validation(dto);
        if (valid.isError())
            return BaseResponse.error(valid.getMessage());

        Optional<Actions> exitsActions = actionsRepository.findByUserIdAndRecordStatusAndActionType(dto.getUserId(), RecordStatus.ACTIVE, dto.getActionType());
        if (exitsActions.isPresent()) {
            dto.setId(exitsActions.get().getId());
            return update(dto);
        } else {
            Actions actions = beanUtil.mapDto(dto, Actions.class);
            Actions model = actionsRepository.save(actions);

            log.info("save actions: {}", model);
            return BaseResponse.success(beanUtil.mapDto(model, ActionsDto.class));
        }
    }


    @Override
    public BaseResponse update(ActionsV0 dto) {
        ValidationDto valid = validation(dto);
        if (valid.isError())
            return BaseResponse.error(valid.getMessage());

        Actions actions = actionsRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        updateField(dto, actions);

        actionsRepository.save(actions);
        log.info("update actions: {}", actions);
        return BaseResponse.success(beanUtil.mapDto(actions, ActionsDto.class));
    }

    @Override
    public BaseResponse delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        comment.setRecordStatus(RecordStatus.DELETED);
        Comment model = commentRepository.save(comment);

        log.info("delete comment: {}", model);
        return BaseResponse.success(model);
    }

    private void updateField(ActionsV0 dto, Actions model) {
        model.setActionType(Objects.isNull(dto.getActionType()) ? model.getActionType() : dto.getActionType());
        model.setActionId(Objects.isNull(dto.getActionId()) ? model.getActionId() : dto.getActionId());
        model.setEmotion(Objects.isNull(dto.getEmotion()) ? model.getEmotion() : dto.getEmotion());
        model.setUserId(Objects.isNull(dto.getUserId()) ? model.getUserId() : dto.getUserId());
        model.setRecordStatus(Objects.isNull(dto.getRecordStatus()) ? model.getRecordStatus() : dto.getRecordStatus());
    }


    private ValidationDto validation(ActionsV0 vo) {
        if (Objects.isNull(vo.getActionId()))
            return ValidationDto.validation(true, "Action is required");
        if (Objects.isNull(vo.getUserId()))
            return ValidationDto.validation(true, "User is required");
        if (Objects.isNull(vo.getActionType()))
            return ValidationDto.validation(true, "Action Type is required");

        if (ActionType.EMOTION.equals(vo.getActionType()) && Objects.isNull(vo.getEmotion()))
            return ValidationDto.validation(true, "Emotion is required");

        if (ActionType.LIKE.equals(vo.getActionType()))
            vo.setEmotion(null);

        Optional<Comment> comment = commentRepository.findById(vo.getActionId());
        if (comment.isEmpty())
            return ValidationDto.validation(true, "Comment not found");

        return ValidationDto.validation(false, "Success");

    }

}
