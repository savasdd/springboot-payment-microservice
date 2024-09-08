package com.payment.notification.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.payment.notification.common.dto.FirebaseData;
import com.payment.notification.common.dto.FirebaseDataRepository;
import com.payment.notification.common.enums.RecordStatus;
import com.payment.notification.common.utils.ConstantUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

@Slf4j
@Getter
@Setter
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "firebase")
public class FirebaseConfig {

    private String configFile;
    private final FirebaseDataRepository repository;

    @Bean
    public void firebaseInit() {
        try {
            FirebaseData firebaseData = repository.findBySenderIdAndRecordStatus(ConstantUtil.FCM_SENDER_ID, RecordStatus.ACTIVE).orElse(null);

            File file = new File(new ClassPathResource(configFile).getURI());
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(file, firebaseData.getData());

            FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(new FileInputStream(file))).build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("FirebaseApp initialized");
            }

        } catch (Exception e) {
            log.error("Error while initializing Firebase {}", e.getMessage());
        }
    }
}
