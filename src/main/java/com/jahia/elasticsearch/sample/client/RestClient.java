package com.jahia.elasticsearch.sample.client;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jahia.elasticsearch.sample.business.repository.AccountRepository;
import com.jahia.elasticsearch.sample.rest.data.Account;
import com.jahia.elasticsearch.sample.rest.data.AccountBuildHelper;
import com.jahia.elasticsearch.sample.util.JahiaUtil;


public class RestClient {
	private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);
	private static final String INDEX_AND_TYPE_PATH = "/accounts/account/";
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private AccountRepository accountRepository;
	
	private String baseUrl;
	
    public RestClient() {
    	try {
			baseUrl = "http://" + JahiaUtil.readYamlConfig().getServer() + ":8080";
		} catch (IOException e) {
			LOG.error("Failed to read YAML file", e);
		}
    }
    
    public Account getAccount(String id) {
    	return restTemplate.getForObject(baseUrl + INDEX_AND_TYPE_PATH, Account.class, id);
    }

	public ResponseEntity<Account> saveAccount(Account account) throws Exception {
		HttpEntity<Account> request = new HttpEntity<>(account);
		return restTemplate.exchange(baseUrl + INDEX_AND_TYPE_PATH, HttpMethod.POST, request, Account.class);
	}
	
	public void removeAccount(String id) throws Exception {
		restTemplate.delete(baseUrl + INDEX_AND_TYPE_PATH + id);
	}
	
	public ResponseEntity<Void> updateAccount(Account account) throws Exception {
		HttpEntity<Account> requestEntity = new HttpEntity<>(account);
		return restTemplate.exchange(baseUrl + INDEX_AND_TYPE_PATH, HttpMethod.PUT, requestEntity, Void.class);
	}
	
	public List<Account> getAllAccounts() {
		return AccountBuildHelper.buildAccounts(accountRepository.findAll());
	}
	
}
