package com.jahia.elasticsearch.sample.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.jahia.elasticsearch.sample.client.RestClient;

@Configuration
public class EsRestClientConfig {
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder templateBuilder) {
		return templateBuilder.build();
	}
	
	@Bean
	public RestClient esClient() {
		return new RestClient();
	}
}
