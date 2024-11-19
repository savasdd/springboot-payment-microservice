package com.payment.service.scheduler;

import com.payment.service.OrderService;
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
public class OutboxScheduler {
    private final OrderService orderService;
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Scheduled(initialDelayString = "${schedulers.outbox.initialDelayMillis}", fixedDelayString = "${schedulers.outbox.fixedRate}")
    private void deleteOutboxRecord() {
        log.info("starting scheduled outbox table publishing: {}", df.format(new Date()));
        orderService.deleteOutboxRecord();
        log.info("completed scheduled outbox table publishing");
    }

}
