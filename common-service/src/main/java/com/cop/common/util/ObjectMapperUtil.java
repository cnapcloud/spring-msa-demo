package com.cop.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class ObjectMapperUtil {

    public static Map<String, Object> convertObjectToMap(Object obj) {
        return getMapper().convertValue(obj, new TypeReference<Map<String, Object>>() {
        });
    }

    public static ObjectMapper getMapper() {
        return new Jackson2ObjectMapperBuilder().featuresToEnable(SerializationFeature.INDENT_OUTPUT)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .modules(new JavaTimeModule(), createCustomModule()).build();
    }

    private static SimpleModule createCustomModule() {
        SimpleModule module = new SimpleModule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));

        return module;
    }

}
