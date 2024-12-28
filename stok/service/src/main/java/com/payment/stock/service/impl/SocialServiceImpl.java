package com.payment.stock.service.impl;

import com.payment.stock.entity.base.BaseSocialDto;
import com.payment.stock.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SocialServiceImpl {
    private CommentRepository commentRepository;

    void setSocialAction(BaseSocialDto dto) {
    }

    void setSocialAction(BaseSocialDto dto, Long userId) {
        dto.setLikeCount(2);
        dto.setLiked(true);
    }

}
