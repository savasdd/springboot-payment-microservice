package com.payment.notification.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SerializerUtil {
    private final ObjectMapper objectMapper;

    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (Exception ex) {
            log.error("error while deserializing data: ${}", ex.getLocalizedMessage());
            throw new RuntimeException(ex.getLocalizedMessage());
        }
    }

    public byte[] serializeToBytes(Object data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception ex) {
            log.error("error while serializing data byte: ${}", ex.getLocalizedMessage());
            throw new RuntimeException(ex.getLocalizedMessage());
        }
    }

    public String serializeToString(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            log.error("error while serializing data string: ${}", ex.getLocalizedMessage());
            throw new RuntimeException(ex.getLocalizedMessage());
        }
    }
}