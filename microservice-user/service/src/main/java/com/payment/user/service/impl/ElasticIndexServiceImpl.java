package com.payment.user.service.impl;

import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.payment.user.common.config.ElasticsearchConfig;
import com.payment.user.common.enums.ElasticIndex;
import com.payment.user.common.utils.BeanUtil;
import com.payment.user.entity.dto.ElasticContent;
import com.payment.user.entity.dto.ElasticUserDto;
import com.payment.user.entity.dto.HouseDto;
import com.payment.user.entity.dto.PersonDto;
import com.payment.user.service.ElasticIndexService;
import com.payment.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ElasticIndexServiceImpl implements ElasticIndexService {

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
                index(esConfig.getIndexUser(), f, f.getId());
            });


        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

    @Override
    public void indexPerson() {
        try {
            DeleteByQueryResponse response = esConfig.getEsConfig().deleteByQuery(q -> q.index(esConfig.getIndexPerson()).query(builder -> builder.matchAll(ma -> ma)));
            log.info("cleared index person {}", response.took());
            PersonDto person1 = PersonDto.builder().id(1L).firstName("Savaş").build();
            person1.setHouses((List.of(HouseDto.builder().id(20L).language("TR").street("Van").build(), HouseDto.builder().id(21L).language("EN").street("London").build())));

            PersonDto person2 = PersonDto.builder().id(2L).firstName("Merve").build();
            person2.setHouses((List.of(HouseDto.builder().id(22L).language("TR").street("Ankara").build(), HouseDto.builder().id(23L).language("TR").street("İstanbul").build())));

            person1.getHouses().forEach(f -> {
                ElasticContent content = ElasticContent.builder().id(f.getId()).firstName(person1.getFirstName()).language(f.getLanguage()).street(f.getStreet()).build();
                index(esConfig.getIndexPerson(), content, content.getId());
            });

            person2.getHouses().forEach(f -> {
                ElasticContent content = ElasticContent.builder().id(f.getId()).firstName(person2.getFirstName()).language(f.getLanguage()).street(f.getStreet()).build();
                index(esConfig.getIndexPerson(), content, content.getId());
            });


        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private void index(String indexName, Object user, Long id) {
        try {
            IndexResponse response = esConfig.getEsConfig().index(i -> i.index(indexName).id(ElasticIndex.USER.getName() + "_" + id).document(user));
            log.info("indexed user {} {}", response.version(), id);
        } catch (Exception ex) {
            log.error("cannot index user {}", ex.getMessage());
        }
    }
}
