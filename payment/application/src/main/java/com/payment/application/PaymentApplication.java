package com.payment.application;

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
@EnableCaching(mode = AdviceMode.ASPECTJ)
@SpringBootApplication(scanBasePackages = "com.payment")
@EntityScan(basePackages = "com.payment")
@EnableJpaRepositories(basePackages = {"com.payment","com.load.base.impl"},repositoryBaseClass = BaseLoadRepository.class)
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}