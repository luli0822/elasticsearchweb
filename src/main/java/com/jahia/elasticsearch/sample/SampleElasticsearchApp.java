package com.jahia.elasticsearch.sample;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.jahia.elasticsearch.sample.business.model.AccountEntity;
import com.jahia.elasticsearch.sample.business.repository.AccountRepository;

/**
 * Bootstrap application
 */
@SpringBootApplication
public class SampleElasticsearchApp {
	
    public static void main(String... args) {
        SpringApplication.run(SampleElasticsearchApp.class, args);
    }
    
    @Bean
    public CommandLineRunner load(AccountRepository repository) {
    	return args -> {
    		repository.save(createEntity("abc123", "RRSP"));
    		repository.save(createEntity("cab111", "RESP"));
    		repository.save(createEntity("bcc100", "RDSP"));
    	};
    }
    
    private AccountEntity createEntity(String docId, String name) {
    	AccountEntity entity = new AccountEntity();
    	entity.setDocSysId(docId);
    	entity.setName(name);
    	return entity;
    }
}
