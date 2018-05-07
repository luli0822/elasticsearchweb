package com.jahia.elasticsearch.sample.rest.data;

import java.util.ArrayList;
import java.util.List;

import com.jahia.elasticsearch.sample.business.model.AccountEntity;

public class AccountBuildHelper {
	private AccountBuildHelper() {}
	
	public static List<Account> buildAccounts(List<AccountEntity> entityList) {
    	List<Account> accountList = new ArrayList<>();
    	for (AccountEntity entity : entityList) {
    		Account account = new Account();
    		account.setId(entity.getDocSysId());
    		account.setName(entity.getName());
    		accountList.add(account);
    	}
		return accountList;
	}
	
	public static AccountEntity buildAccountEntity(Account account) {
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setDocSysId(account.getId());
		accountEntity.setName(account.getName());
		return accountEntity;
	}
}
