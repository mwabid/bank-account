package com.sg.bank.account.springconf;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.rabitka.core.ddd.Validatable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

@Configuration
public class JacksonMappingConfig {


    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter() {
            @Override
            public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException {
                return super.read(type, contextClass, inputMessage);
            }
        };
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule jsonModule = new SimpleModule();
        jsonModule.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (Validatable.class.isAssignableFrom(beanDesc.getType().getRawClass())) {
                    return new CustomBeanDeserializer((BeanDeserializerBase) deserializer);
                }

                return super.modifyDeserializer(config, beanDesc, deserializer);
            }
        });
        objectMapper.registerModule(jsonModule);
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }
}
