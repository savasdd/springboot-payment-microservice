package com.payment.stock.application;

import com.load.base.impl.BaseLoadRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@EnableScheduling
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.payment.stock")
@EntityScan(basePackages = "com.payment.stock")
@EnableJpaRepositories(basePackages = {"com.payment.stock","com.load.base.impl"},repositoryBaseClass = BaseLoadRepository.class)
@EnableCaching(mode = AdviceMode.ASPECTJ)
public class StockApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }
}