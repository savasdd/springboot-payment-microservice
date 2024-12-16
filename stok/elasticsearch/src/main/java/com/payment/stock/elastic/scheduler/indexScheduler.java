package com.payment.stock.elastic.scheduler;

import com.payment.stock.elastic.IndexService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(prefix = "schedulers", value = {"outbox.enable"}, havingValue = "true")
public class indexScheduler {
    private final IndexService indexService;
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Scheduled(initialDelayString = "${schedulers.outbox.initialDelayMillis}", fixedDelayString = "${schedulers.outbox.fixedRate}")
    private void indexStock() {
        log.info("starting scheduled indexStock: {}", df.format(new Date()));
        indexService.index();
        log.info("completed scheduled indexStock");
    }

}