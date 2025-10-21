package com.cop.common.jackson;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.SpringDataJacksonConfiguration;
import org.springframework.data.web.config.SpringDataWebSettings;
import org.springframework.lang.Nullable;

import com.cop.common.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(@Nullable SpringDataWebSettings settings) {
        ObjectMapper mapper = ObjectMapperUtil.getMapper();
        mapper.registerModules(new SpringDataJacksonConfiguration.PageModule(settings));

        return mapper;
    }
}
