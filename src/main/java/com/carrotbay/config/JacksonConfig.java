package com.carrotbay.config;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		Hibernate5Module hibernate5Module = new Hibernate5Module();

		hibernate5Module.configure(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		objectMapper.registerModule(hibernate5Module);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
		return objectMapper;
	}
}