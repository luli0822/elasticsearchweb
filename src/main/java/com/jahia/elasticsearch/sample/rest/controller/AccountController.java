package com.jahia.elasticsearch.sample.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jahia.elasticsearch.sample.business.service.AccountService;
import com.jahia.elasticsearch.sample.exceptions.SystemProcessException;
import com.jahia.elasticsearch.sample.rest.data.Account;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Controller that handles all REST APIs for {@link com.jahia.elasticsearch.sample.business.model.AccountEntity}
 */
@RestController
public class AccountController {
	private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);
    private AccountService accountService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @GetMapping(value = "/accounts/account/_search?pretty=true")
    public ResponseEntity<Object[]> findAllAccounts() {
    	try {
        	ActionResponse actionResponse = accountService.getAllAccounts();
        	if (null != actionResponse) {
        		SearchResponse searchResponse = (SearchResponse) actionResponse;
        		if (searchResponse.getHits().getTotalHits() > 0) {
        			List<Account> accounts = new ArrayList<>();
        			for (SearchHit hit : searchResponse.getHits()) {
        				accounts.add(objectMapper.convertValue(hit.getSourceAsMap(), Account.class));
        			}
        			return ResponseEntity.ok(accounts.toArray());
        		}
        	}
		} catch (Exception e) {
	        LOG.error("Error while retrieving accounts under index", e);
	        throw new SystemProcessException(e);
		}
        return ResponseEntity.notFound().build();
    }

    /**
     *
     * @param id
     * @return {@link AccountEntity} based off of the input id
     */
    @RequestMapping(value = "/accounts/account/{id}", method = RequestMethod.GET)
    public ResponseEntity<Account> findAccount(@PathVariable(value = "id") String id) {
        try {
        	ActionResponse actionResponse = accountService.findAccountById(id);
        	if (null != actionResponse) {
        		Map<String, Object> datamap = ((GetResponse) actionResponse).getSourceAsMap();
        		Account account = objectMapper.convertValue(datamap, Account.class);
        		return ResponseEntity.ok(account);
        	}
        	
		} catch (Exception e) {
	        LOG.error("Error while retrieving account with id = " + id, e);
	        return processElasticsearchException(e);
		}
        return ResponseEntity.notFound().build();
    }

    /**
     * Update an Account based off of the input id
     * @param account
     * 
     */
    @RequestMapping(value = "/accounts/account", method = RequestMethod.PUT)
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        try {
			ActionResponse actionResponse = accountService.updateAccount(account);
			if (null != actionResponse) {
				UpdateResponse updateResponse = (UpdateResponse) actionResponse;
				if (DocWriteResponse.Result.UPDATED == updateResponse.getResult()) {
					return ResponseEntity.noContent().build();					
				} else {
					throw new SystemProcessException();
				}
			}
		} catch (Exception e) {
			LOG.error("Error while updating the account " + account, e);
			return processElasticsearchException(e);
		}
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(value = "/accounts/account/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Account> removeAccount(@PathVariable(value = "id") String id) {
        try {
			ActionResponse actionResponse = accountService.removeAccount(id);
			if (null != actionResponse) {
				DeleteResponse deleteResponse = (DeleteResponse) actionResponse;
				if (Result.NOT_FOUND == deleteResponse.getResult()) {
					throw new SystemProcessException();
				}
				return ResponseEntity.noContent().build();
			}
		} catch (Exception e) {
			LOG.error("Error while removing the account with id " + id, e);
			return processElasticsearchException(e);
		}
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    @RequestMapping(value = "/accounts/account", method = RequestMethod.POST)
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
    	try {
			ActionResponse actionResponse = accountService.saveAccount(account);
			if (null != actionResponse) {
				return ResponseEntity.status(HttpStatus.CREATED).build();
			}
		} catch (Exception e) {
			LOG.error("Error while saving " + account, e);
			return processElasticsearchException(e);
		}
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    private ResponseEntity<Account> processElasticsearchException(Throwable e) {
    	if (e instanceof ElasticsearchException) {
			ElasticsearchException searchException = (ElasticsearchException) e;
			return parseErrorResponse(searchException.status().getStatus());
		} 
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    private ResponseEntity<Account> parseErrorResponse(int statusCode) {
    	if (RestStatus.BAD_REQUEST.getStatus() == statusCode) {
    		return ResponseEntity.badRequest().build();
    	} else if (RestStatus.NOT_FOUND.getStatus() == statusCode) {
    		return ResponseEntity.notFound().build();
    	} 
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    
}
