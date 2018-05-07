package com.jahia.elasticsearch.sample.business.service;

import com.jahia.elasticsearch.sample.rest.data.Account;

import java.util.List;

import org.elasticsearch.action.ActionResponse;

/**
 * Service class to process CRUD operations towards to ES
 */
public interface AccountService {
	/**
	 * Find an account based off of passed-in id
	 * @param id
	 * @return {@link ActionResponse}
	 */
    ActionResponse findAccountById(String id) throws Exception;
    
    /**
     * 
     * @return all accounts under index "accounts"
     * @throws Exception
     */
    ActionResponse getAllAccounts() throws Exception;
    
    /**
     * Persist a new account in ES
     * @param account
     * @return {@link ActionResponse}
     */
    ActionResponse saveAccount(Account account) throws Exception;
    
    /**
     * Remove an account from ES
     * @param id
     * @return {@link ActionResponse}
     */
    ActionResponse removeAccount(String id) throws Exception;
    
    /**
     * Update information for an exiting account
     * @param account
     * @return {@link ActionResponse}
     */
    ActionResponse updateAccount(Account account) throws Exception;
}
