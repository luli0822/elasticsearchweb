package com.jahia.elasticsearch.sample.ui;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.jahia.elasticsearch.sample.business.repository.AccountRepository;
import com.jahia.elasticsearch.sample.client.RestClient;
import com.jahia.elasticsearch.sample.rest.data.Account;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI
public class SearchRootView extends UI {
	private static final long serialVersionUID = 4572685310916797976L;
	
	private VerticalLayout root;
	
	@Autowired
	private RestClient restClient;
	
	@Autowired
	private AccountRepository repository;
	
	@Override
	protected void init(VaadinRequest request) {
		layout();
		addHeader();
		addContent();
	}
	
	private void addContent() {
		List<Account> accountList = restClient.getAllAccounts();
		for (Account account : accountList) {
			root.addComponent(new AccountItemLayout(repository, restClient, account));
		}
	}

	private void layout() {
		root = new VerticalLayout();
		root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setContent(root);
	}
	
	private void addHeader() {
		Label header = new Label("My Accounts");
		header.addStyleName(ValoTheme.LABEL_H2);
		root.addComponent(header);
	}
		
}
