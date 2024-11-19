package com.payment.user.elastic.impl;

import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.payment.user.common.config.ElasticsearchConfig;
import com.payment.user.common.enums.ElasticIndex;
import com.payment.user.common.utils.BeanUtil;
import com.payment.user.elastic.IndexService;
import com.payment.user.entity.dto.ElasticUserDto;
import com.payment.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IndexServiceImpl implements IndexService {

    private final UserService userService;
    private final ElasticsearchConfig esConfig;
    private final BeanUtil beanUtil;

    @Override
    public void indexUser() {
        try {
            DeleteByQueryResponse response = esConfig.getEsConfig().deleteByQuery(q -> q.index(esConfig.getIndexUser()).query(builder -> builder.matchAll(ma -> ma)));
            log.info("cleared index user {}", response.took());

            List<ElasticUserDto> userList = beanUtil.mapAll(userService.findAllDto(Pageable.ofSize(20)), ElasticUserDto.class);

            userList.forEach(f -> {
                f.setContentType(ElasticIndex.USER.getName());
                f.setContentId(ElasticIndex.USER.getCode());
                index(esConfig.getIndexUser(), f);
            });


        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }

    }

    private void index(String indexName, ElasticUserDto user) {
        try {
            IndexResponse response = esConfig.getEsConfig().index(i -> i.index(indexName).id(ElasticIndex.USER.getName() + "_" + user.getId()).document(user));
            log.info("indexed user {} {}", response.version(), user.getId());
        } catch (Exception ex) {
            log.error("cannot index user {}", ex.getMessage());
        }
    }
}
