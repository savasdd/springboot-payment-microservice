package com.payment.stock.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.payment.stock.entity.model.CDNData;
import com.payment.stock.repository.CDNRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;

@Slf4j
@Getter
@Setter
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cdn")
public class CDNConfig {

    private String configFile;
    private String bucketName;
    private final CDNRepository repository;
    private final ObjectMapper objectMapper;

    @Bean
    public Bucket firebaseInit() {
        try {
            CDNData data = repository.getActiveCDN().orElse(null);

            File file = new File(new ClassPathResource(configFile).getURI());
            objectMapper.writeValue(file, data.getData());

            StorageOptions options = StorageOptions.newBuilder().setProjectId(data.getData().getProject_id()).setCredentials(GoogleCredentials.fromStream(new FileInputStream(file))).build();
            Storage storage = options.getService();
            Bucket bucket = storage.get(bucketName, Storage.BucketGetOption.fields());

            log.info("CDN initialized");
            return bucket;
        } catch (Exception e) {
            log.error("Error while initializing Firebase {}", e.getMessage());
            return null;
        }
    }
}
