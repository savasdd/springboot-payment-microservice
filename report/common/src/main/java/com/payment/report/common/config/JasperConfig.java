package com.payment.report.common.config;

import com.payment.report.common.content.ReportContentFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasperConfig {

    @Bean
    public ReportContentFilter filter() {
        return new ReportContentFilter();
    }

    //TODO init DataSource

}
