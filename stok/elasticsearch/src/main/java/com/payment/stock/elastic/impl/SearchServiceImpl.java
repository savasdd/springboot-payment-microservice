package com.payment.stock.elastic.impl;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SearchType;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.MsearchRequest;
import co.elastic.clients.elasticsearch.core.MsearchResponse;
import com.load.impl.DataLoad;
import com.payment.stock.cdn.CdnService;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.config.ElasticsearchConfig;
import com.payment.stock.common.enums.ElasticIndex;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.elastic.SearchService;
import com.payment.stock.entity.dto.ElasticContent;
import com.payment.stock.entity.dto.ImageInfoDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SearchServiceImpl implements SearchService {
    private static final List<String> SEARCH_FIELDS = List.of("stockName", "unitType", "percent", "rateName");
    private final ElasticsearchConfig esConfig;
    private final CdnService cdnService;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse search(String searchText, DataLoad load) {
        StringUtils.stripAccents(searchText);

        try {
            List<ElasticContent> contents = new ArrayList<>();
            MsearchResponse<ElasticContent> response = esConfig.getEsConfig().msearch(query(searchText, load.getTake()), ElasticContent.class);

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

    //TODO add to number query
    private MsearchRequest query(String searchIndex, int pageSize) {
        return Objects.isNull(searchIndex) ? searchAll(pageSize) : searchString(searchIndex, pageSize);
    }

    private MsearchRequest searchString(String searchIndex, int pageSize) {
        return MsearchRequest.of(of -> of.searches(s -> s
                .body(bd -> bd
                        .query(q -> q
                                .bool(b -> b.must(m -> m.queryString(mm -> mm
                                                .query("*" + searchIndex + "*")
                                                .fields(SEARCH_FIELDS).defaultOperator(Operator.Or)
                                                .fuzzyTranspositions(false)))
                                        .filter(f -> f.terms(tf -> tf.field("contentId").terms(fs -> fs.value(List.of(FieldValue.of(ElasticIndex.STOCK.getCode()))))))
                                ))
                        .size(pageSize)).header(h -> h.index(esConfig.getIndexStock()))).searchType(SearchType.DfsQueryThenFetch));
    }

    private MsearchRequest searchAll(int pageSize) {
        return MsearchRequest.of(of -> of.searches(s -> s
                .body(bd -> bd.query(q -> q.bool(b ->b.filter(f -> f.terms(tf -> tf.field("contentId").terms(fs -> fs.value(List.of(FieldValue.of(ElasticIndex.STOCK.getCode()))))))))
                        .size(pageSize)).header(h -> h.index(esConfig.getIndexStock()))).searchType(SearchType.DfsQueryThenFetch));
    }

    private String getImage(Long stockId) {
        BaseResponse response = cdnService.getImage(stockId);
        List<ImageInfoDto> dtoList = beanUtil.mapAll(List.of(response.getData()), ImageInfoDto.class, ImageInfoDto.class);
        return !dtoList.isEmpty() ? dtoList.stream().findFirst().get().getImage() : null;
    }
}
