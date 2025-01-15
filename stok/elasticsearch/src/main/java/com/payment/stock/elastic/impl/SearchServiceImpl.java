package com.payment.stock.elastic.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SearchType;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.MsearchRequest;
import co.elastic.clients.elasticsearch.core.MsearchResponse;
import com.load.impl.DataLoad;
import com.payment.stock.cdn.CdnService;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.config.ElasticsearchConfig;
import com.payment.stock.common.enums.IndexType;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.elastic.SearchService;
import com.payment.stock.entity.dto.ElasticContent;
import com.payment.stock.entity.dto.ImageInfoDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SearchServiceImpl implements SearchService {
    private static final List<String> SEARCH_FIELDS = List.of("stockName", "percent", "rateName", "categoryName");
    private final ElasticsearchConfig esConfig;
    private final CdnService cdnService;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse search(Integer year, Long category, String searchText, DataLoad load) {
        StringUtils.stripAccents(searchText);

        try {
            List<ElasticContent> contents = new ArrayList<>();
            MsearchResponse<ElasticContent> response = esConfig.getEsConfig().msearch(query(year, category, searchText, load.getTake()), ElasticContent.class);

            response.responses().forEach(f -> {
                if (!f.isFailure()) {
                    f.result().hits().hits().forEach(h -> {
                        h.source().setImage(getImage(h.source().getId()));
                        contents.add(h.source());
                    });
                }
            });

            log.info("elasticsearch search result: {}", contents);
            return BaseResponse.success(contents, (long) contents.size());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private MsearchRequest query(Integer year, Long category, String searchIndex, int pageSize) {
        return Objects.isNull(searchIndex) ? searchAll(year, category, pageSize) : searchString(year, category, searchIndex, pageSize);
    }

    private MsearchRequest searchString(Integer year, Long category, String searchIndex, int pageSize) {
        return MsearchRequest.of(of -> of.searches(s -> s
                .body(bd -> bd
                        .query(q -> q
                                .bool(b -> b.must(m -> m.queryString(mm -> mm
                                                .query("*" + searchIndex + "*")
                                                .fields(SEARCH_FIELDS).defaultOperator(Operator.Or)
                                                .fuzzyTranspositions(false)))
                                        .filter(getQueries(year, category))
                                ))
                        .size(pageSize)).header(h -> h.index(esConfig.getIndexStock()))).searchType(SearchType.DfsQueryThenFetch));
    }

    private MsearchRequest searchAll(Integer year, Long category, int pageSize) {
        return MsearchRequest.of(of -> of.searches(s -> s.body(bd -> bd.query(q -> q.bool(b -> b.filter(getQueries(year, category))))
                .size(pageSize)).header(h -> h.index(esConfig.getIndexStock()))).searchType(SearchType.DfsQueryThenFetch));
    }

    private static List<Query> getQueries(Integer year, Long category) {
        List<Query> queries = new ArrayList<>();
        queries.add(getBoolQuery("contentId", IndexType.STOCK.getCode()));
        queries.add(getBoolQuery("year", year));
        if (!Objects.isNull(category)) {
            queries.add(getBoolQuery("categoryId", category));
        }
        return queries;
    }

    private static Query getBoolQuery(String field, Object value) {
        return BoolQuery.of(b -> b.filter(f -> f.terms(tf -> tf.field(field).terms(fs -> fs.value(List.of(FieldValue.of(value)))))))._toQuery();
    }

    private String getImage(Long stockId) {
        BaseResponse response = cdnService.getImage(stockId);
        List<ImageInfoDto> dtoList = beanUtil.mapAll(List.of(response.getData()), ImageInfoDto.class, ImageInfoDto.class);
        return !dtoList.isEmpty() ? dtoList.stream().findFirst().get().getImage() : null;
    }

    //todo not working
    private MsearchRequest searchAllOld(Integer year, Long category, int pageSize) {
        return MsearchRequest.of(of -> of.searches(s -> s
                .body(bd -> bd.query(q -> q.bool(b ->
                                b.filter(f -> f.terms(tf -> tf.field("contentId").terms(fs -> fs.value(List.of(FieldValue.of(IndexType.STOCK.getCode()))))))
                                        .filter(f -> f.terms(tf -> tf.field("year").terms(fs -> fs.value(List.of(FieldValue.of(year))))))
                                        .filter(f -> !Objects.isNull(category) ? f.terms(tf -> tf.field("categoryId").terms(fs -> fs.value(List.of(FieldValue.of(category))))) : f.terms(tf -> tf.field("categoryId").terms(fs -> fs.value(List.of()))))
                        ))
                        .size(pageSize)).header(h -> h.index(esConfig.getIndexStock()))).searchType(SearchType.DfsQueryThenFetch));
    }
}
