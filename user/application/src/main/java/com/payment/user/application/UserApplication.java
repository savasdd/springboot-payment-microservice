package com.payment.user.application;

import com.load.base.impl.BaseLoadRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "com.payment.user")
@EntityScan(basePackages = "com.payment.user")
@EnableJpaRepositories(basePackages = {"com.payment.user","com.load.base.impl"},repositoryBaseClass = BaseLoadRepository.class)
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}