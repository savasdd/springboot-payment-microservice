package com.payment.user.elastic.impl;

import co.elastic.clients.elasticsearch._types.SearchType;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.MsearchRequest;
import co.elastic.clients.elasticsearch.core.MsearchResponse;
import com.payment.user.common.base.BaseResponse;
import com.payment.user.common.config.ElasticsearchConfig;
import com.payment.user.elastic.SearchService;
import com.payment.user.entity.dto.ElasticUserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SearchServiceIImpl implements SearchService {

    private final ElasticsearchConfig esConfig;

    @Override
    public BaseResponse searchUser(String searchIndex, Pageable searchPage) {
        try {
            List<ElasticUserDto> contentDto = new ArrayList<>();
            MsearchResponse<ElasticUserDto> searchResponse = esConfig.getEsConfig().msearch(queryUser(searchIndex, searchPage.getPageSize()), ElasticUserDto.class);

            searchResponse.responses().forEach(f -> {
                if (!f.isFailure()) {
                    f.result().hits().hits().forEach(h -> {
                        ElasticUserDto dto = h.source();
                        contentDto.add(dto);
                    });
                }
            });
            return BaseResponse.builder().data(contentDto).count((int) searchResponse.took()).build();

        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }

    }

    private MsearchRequest queryUser(String searchIndex, int pageSize) {
        return MsearchRequest.of(of -> of.searches(s -> s
                .body(bd -> bd
                        .query(q -> q
                                .bool(b -> b.must(m -> m.queryString(mm -> mm
                                        .query("*" + StringUtils.stripAccents(searchIndex).toLowerCase() + "*")
                                        .fields(List.of("username", "firstName", "lastName", "email", "address")).defaultOperator(Operator.Or)
                                        .fuzzyTranspositions(false)))
                                ))
                        .size(pageSize)).header(h -> h.index(esConfig.getIndexUser()))).searchType(SearchType.DfsQueryThenFetch));
    }
}
