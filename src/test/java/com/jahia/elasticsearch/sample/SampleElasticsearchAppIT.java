package com.jahia.elasticsearch.sample;

import com.jahia.elasticsearch.sample.rest.data.Account;
import com.jahia.elasticsearch.sample.util.JahiaUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Integration test for application
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleElasticsearchAppIT {
	private static final String MOCK_DOC_ID = "abc123";
	
    @LocalServerPort
    private int port;

    
    private String baseLocalUrl;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void init() throws IOException {
    	baseLocalUrl = JahiaUtil.readYamlConfig().getServer() + port;
    }
    
    private String getResourcePath() {
    	return baseLocalUrl + "/accounts/account/";
    }

    @Test
    public void verifyFindAccount() {
        Account account = mockEntity(MOCK_DOC_ID, "RESP");
        ResponseEntity<Account> responseEntity = template.getForEntity(getResourcePath() + account.getId(), Account.class, MOCK_DOC_ID);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
    }
    
    @Test
    public void verifyUpdateAccount() {
    	Account account = mockEntity(MOCK_DOC_ID, "RRSP");
        HttpEntity<Account> requestEntity = new HttpEntity<>(account);
        ResponseEntity<Void> response = template.exchange(getResourcePath(), HttpMethod.PUT, requestEntity, Void.class);
        assertNotNull(response);
    }
    
    @Test
    public void verifyRemoveAccount() {
    	template.delete(getResourcePath(), MOCK_DOC_ID);
    }
    
    @Test
    public void verifyCreateAccount() {
    	HttpEntity<Account> request = new HttpEntity<>(mockEntity(UUID.randomUUID().toString(), "RRSP"));
    	ResponseEntity<Account> response = template.exchange(getResourcePath(), HttpMethod.POST, request, Account.class);
    	assertNotNull(response);
    	assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }
    
    private Account mockEntity(String id, String name) {
    	Account account = new Account();
    	account.setId(id);
    	account.setName(name);
    	return account;
    }
    
}
