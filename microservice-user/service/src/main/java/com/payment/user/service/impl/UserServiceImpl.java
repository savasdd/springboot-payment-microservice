package com.payment.user.service.impl;

import com.payment.user.common.base.BaseResponse;
import com.payment.user.common.config.kafka.KafkaTopicsConfig;
import com.payment.user.common.utils.BeanUtil;
import com.payment.user.common.utils.CacheUtil;
import com.payment.user.common.utils.ConstantUtil;
import com.payment.user.entity.content.KafkaContent;
import com.payment.user.entity.dto.UserDto;
import com.payment.user.entity.model.User;
import com.payment.user.entity.vo.UserVo;
import com.payment.user.repository.CityRepository;
import com.payment.user.repository.UserRepository;
import com.payment.user.service.UserService;
import com.payment.user.service.publisher.NotifySerializer;
import com.payment.user.service.publisher.Publisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final CityRepository cityRepository;
    private final NotifySerializer notifySerializer;
    private final KafkaTopicsConfig topicsConfig;
    private final PasswordEncoder passwordEncoder;
    private final Publisher publisher;
    private final BeanUtil beanUtil;

    @Cacheable(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, unless = "#result == null || #result.count == 0")
    @Override
    public BaseResponse findAll(Pageable pageable) {
        Page<UserDto> list = beanUtil.mapAll(repository.findAll(pageable), UserDto.class);

        log.info("get all user {}", list.getTotalElements());
        return BaseResponse.builder().data(list.getContent()).count((int) list.getTotalElements()).build();
    }

    @Override
    public List<UserDto> findAllDto(Pageable pageable) {
        Page<UserDto> list = beanUtil.mapAll(repository.findAll(pageable), UserDto.class);
        return list.getContent();
    }

    @Cacheable(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, key = "#id", unless = "#result == null || #result.count == 0")
    @Override
    public BaseResponse findById(Long id) {
        return BaseResponse.builder().data(beanUtil.mapDto(repository.findById(id).orElse(null), UserDto.class)).build();
    }

    @CacheEvict(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, allEntries = true)
    @Override
    public BaseResponse save(UserVo dto) {
        User user = beanUtil.mapDto(dto, User.class);
        user.setCity(cityRepository.findById(dto.getCity().getId()).orElse(null));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User model = repository.save(user);

        log.info("save user: {}", model);
        publishNotification(user.getId(), ConstantUtil.USER_CREATE);
        return BaseResponse.builder().data(beanUtil.mapDto(model, UserDto.class)).build();
    }

    @CacheEvict(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, allEntries = true)
    @Override
    public BaseResponse update(Long id, UserVo dto) {
        User user = beanUtil.transform(dto, repository.findById(id).orElse(null));
        user.setCity(cityRepository.findById(dto.getCity().getId()).orElse(null));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User model = repository.save(user);

        log.info("update user: {}", model);
        publishNotification(user.getId(), ConstantUtil.USER_UPDATE);
        return BaseResponse.builder().data(beanUtil.mapDto(model, UserDto.class)).build();
    }

    @CacheEvict(cacheManager = CacheUtil.CACHE_MANAGER, cacheNames = CacheUtil.CACHE_NAME, allEntries = true)
    @Override
    public BaseResponse delete(Long id) {
        repository.deleteById(id);

        log.info("delete user: {}", id);
        publishNotification(id, ConstantUtil.USER_DELETE);
        return BaseResponse.builder().build();
    }

    private void publishNotification(Long userId, String message) {
        try {
            KafkaContent event = notifySerializer.notification(UUID.randomUUID().toString(), String.valueOf(userId), message);
            log.info("publishing notification event: {}", event);
            publisher.publish(topicsConfig.getTopicName(event.getEventType()), event.getAggregateId(), event);

            log.info("notification event published: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("exception while publishing notification    event: {}", e.getLocalizedMessage());
        }
    }
}
