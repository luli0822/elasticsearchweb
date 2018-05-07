package com.jahia.elasticsearch.sample.ui;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jahia.elasticsearch.sample.client.RestClient;
import com.jahia.elasticsearch.sample.exceptions.SystemProcessException;
import com.jahia.elasticsearch.sample.rest.data.Account;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI
public class SearchRootView extends UI {
	private static final long serialVersionUID = 4572685310916797976L;
	private static final Logger LOG = LoggerFactory.getLogger(SearchRootView.class);
	
	private VerticalLayout root;
	private Grid<Account> grid;
	private PopupView popUp;
	private Button save;
	private Button update;
	private Button delete;
	
	private Set<Account> selectedAccounts;
	private Account selectedAccount;
	
	@Autowired
	private RestClient restClient;
	
	@Override
	protected void init(VaadinRequest request) {
		layout();
		addHeader();
		addContentList();
		addButtons();
	}
	
	private void addButtons() {
		HorizontalLayout footer = new HorizontalLayout();
		save = new Button("Save");
		save.addStyleName(ValoTheme.BUTTON_PRIMARY);
		save.addClickListener(click -> {
			selectedAccounts.forEach(account -> {
				try {
					restClient.saveAccount(account);
					Notification.show("Account saved in Elasticsearch Server successfully!");
				} catch (Exception e) {
					LOG.error("Exception while creating " + account, e);
					Notification.show("Failed to save " + account);
				}
				
			});
		});
		
		update = new Button("Update");
		update.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		update.addClickListener(click -> {
			if (selectedAccounts.size() == 1) {
				selectedAccount = (Account) selectedAccounts.toArray()[0];
				popUp.setPopupVisible(true);
			} else {
				Notification.show("Only one Account can be updated from Elasticsearch.");
			}
		});
	
		delete = new Button("Delete");
		delete.addStyleName(ValoTheme.BUTTON_DANGER);
		delete.setIcon(VaadinIcons.DEL);
		delete.addClickListener(click -> {
			selectedAccounts.forEach(account -> {
				try {
					restClient.removeAccount(account.getId());
					Notification.show("Account removed from Elasticsearch successfully");
				} catch (Exception e) {
					LOG.error("Exception while removing " + selectedAccount, e);
					Notification.show(account + " not found in datastore!");
				}
			});
		});
		footer.addComponents(save, update, delete);
		root.addComponent(footer);
	}
	
	private PopupView createPopup() {
		HorizontalLayout popupContent = new HorizontalLayout();
		TextField name = new TextField();
		popupContent.addComponent(name);
		Button ok = new Button("OK", click -> {
			selectedAccount.setName(name.getValue());
			try {
				restClient.updateAccount(selectedAccount);
				Notification.show("Update successful for " + selectedAccount);
				popUp.setPopupVisible(false);
			} catch (Exception e) {
				LOG.error("Exception while updating " + selectedAccount, e);
				Notification.show("Update failed as " + selectedAccount + " not found in datastore!");
			}
			
		});
		PopupView popup = new PopupView(null, popupContent);
		popupContent.addComponent(ok);
		return popup;		
	}

	private void layout() {
		root = new VerticalLayout();
		root.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		popUp = createPopup();
		root.addComponent(popUp);
		setContent(root);
	}
	
	private void addHeader() {
		Label header = new Label("My Accounts");
		header.addStyleName(ValoTheme.LABEL_H2);
		root.addComponent(header);
	}
	
	private void addContentList() {
		grid = new Grid<>(Account.class);
		grid.setItems(restClient.getAllAccounts());
		grid.addSelectionListener(event -> {
			selectedAccounts = event.getAllSelectedItems();
			selectedAccount = (Account) selectedAccounts.toArray()[0];
		});
		root.addComponent(grid);
	}
	
}
