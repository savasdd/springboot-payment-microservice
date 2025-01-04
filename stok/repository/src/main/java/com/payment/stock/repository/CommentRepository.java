package com.payment.stock.repository;

import com.load.base.BaseRepository;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.entity.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment>, BaseRepository<Comment, Long> {

    Optional<Comment> findByIdAndUserIdAndRecordStatus(Long id, Long userId, RecordStatus status);


}
