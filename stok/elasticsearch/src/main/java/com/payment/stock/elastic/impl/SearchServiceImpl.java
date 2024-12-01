package com.payment.stock.elastic.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SearchType;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.MsearchRequest;
import co.elastic.clients.elasticsearch.core.MsearchResponse;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.config.ElasticsearchConfig;
import com.payment.stock.common.enums.ElasticIndex;
import com.payment.stock.elastic.SearchService;
import com.payment.stock.entity.dto.ElasticContent;
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
public class SearchServiceImpl implements SearchService {
    private final ElasticsearchConfig esConfig;

    @Override
    public BaseResponse search(String searchText, Pageable pageable) {
        StringUtils.stripAccents(searchText);

        try {
            List<ElasticContent> contents = new ArrayList<>();
            MsearchResponse<ElasticContent> response = esConfig.getEsConfig().msearch(query(searchText, pageable.getPageSize()), ElasticContent.class);

            response.responses().forEach(f -> {
                if (!f.isFailure()) {
                    f.result().hits().hits().forEach(h -> contents.add(h.source()));
                }
            });

            log.info("elasticsearch search result: {}", contents);
            return BaseResponse.success(contents, (long) contents.size());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private MsearchRequest query(String searchIndex, int pageSize) {
        return MsearchRequest.of(of -> of.searches(s -> s
                .body(bd -> bd
                        .query(q -> q
                                .bool(b -> b.must(m -> m.queryString(mm -> mm
                                                .query("*" + searchIndex + "*")
                                                .fields(List.of("stockName", "unitType", "paymentType")).defaultOperator(Operator.Or)
                                                .fuzzyTranspositions(false)))
                                        .filter(f -> f.terms(tf -> tf.field("contentId").terms(fs -> fs.value(List.of(FieldValue.of(ElasticIndex.STOCK.getCode()))))))
                                ))
                        .size(pageSize)).header(h -> h.index(esConfig.getIndexStock()))).searchType(SearchType.DfsQueryThenFetch));
    }
}
