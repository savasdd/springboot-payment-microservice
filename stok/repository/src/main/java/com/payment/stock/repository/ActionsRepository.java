package com.payment.stock.repository;

import com.load.base.BaseRepository;
import com.payment.stock.common.enums.ActionType;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.entity.model.Actions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ActionsRepository extends JpaRepository<Actions, Long>, JpaSpecificationExecutor<Actions>, BaseRepository<Actions, Long> {

    Optional<Actions> findByUserIdAndRecordStatusAndActionType(Long userId, RecordStatus recordStatus, ActionType actionType);

}
