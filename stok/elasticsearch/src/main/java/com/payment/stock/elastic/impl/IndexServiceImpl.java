package com.payment.stock.elastic.impl;

import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.load.impl.DataLoad;
import com.payment.stock.cdn.CdnService;
import com.payment.stock.common.base.BaseResponse;
import com.payment.stock.common.config.ElasticsearchConfig;
import com.payment.stock.common.enums.ElasticIndex;
import com.payment.stock.common.enums.RecordStatus;
import com.payment.stock.common.utils.BeanUtil;
import com.payment.stock.elastic.IndexService;
import com.payment.stock.entity.dto.ElasticContent;
import com.payment.stock.entity.dto.ImageInfoDto;
import com.payment.stock.entity.dto.StockDto;
import com.payment.stock.entity.dto.StockRateDto;
import com.payment.stock.entity.model.Stock;
import com.payment.stock.service.StockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IndexServiceImpl implements IndexService {
    private final StockService stockService;
    private final ElasticsearchConfig esConfig;
    private final BeanUtil beanUtil;

    @Override
    public BaseResponse index() {
        try {
            deleteIndex();
            Pageable pageable = PageRequest.of(0, 100);

            do {
                Page<StockDto> response = stockService.getPageable(pageable);
                List<ElasticContent> contents = beanUtil.mapAll(response.getContent(), ElasticContent.class);

                contents.forEach(content -> {
                    StockRateDto dto = response.stream().filter(f -> f.getId().equals(content.getId())).toList().stream().findFirst().orElseThrow().getRate();
                    content.setContentType(ElasticIndex.STOCK.getName());
                    content.setContentId(ElasticIndex.STOCK.getCode());
                    content.setRateName(dto.getRateName());
                    content.setPercent(dto.getPercent());
                    index(esConfig.getIndexStock(), content);
                });

                pageable = response.nextPageable();
            } while (pageable.isPaged());


            return BaseResponse.success("Success");
        } catch (Exception e) {
            log.error("Exception occurred while indexing data", e);
        }

        return BaseResponse.error("Exception occurred while indexing data");
    }

    private void index(String indexName, ElasticContent content) {
        try {
            IndexResponse response = esConfig.getEsConfig().index(i -> i.index(indexName).id(ElasticIndex.STOCK.getName() + "_" + content.getId()).document(content));
            log.info("indexed stock {} {}", response.version(), content.getId());
        } catch (Exception ex) {
            log.error("cannot index stock {}", ex.getMessage());
        }
    }


    private void deleteIndex() {
        try {
            DeleteByQueryResponse response = esConfig.getEsConfig().deleteByQuery(q -> q.index(esConfig.getIndexStock()).query(builder -> builder.matchAll(ma -> ma)));
            log.info("cleared index: {} response: {}", esConfig.getIndexStock(), response.took());
        } catch (Exception e) {
            log.error("cannot delete index {}", e.getMessage());
        }
    }
}
