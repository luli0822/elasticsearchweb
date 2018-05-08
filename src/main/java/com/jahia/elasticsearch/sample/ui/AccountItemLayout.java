package com.jahia.elasticsearch.sample.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jahia.elasticsearch.sample.business.model.AccountEntity;
import com.jahia.elasticsearch.sample.business.repository.AccountRepository;
import com.jahia.elasticsearch.sample.client.RestClient;
import com.jahia.elasticsearch.sample.rest.data.Account;
import com.jahia.elasticsearch.sample.rest.data.AccountBuildHelper;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Layout with each {@link Account} and related actions
 *
 */
public class AccountItemLayout extends HorizontalLayout {
	private static final long serialVersionUID = -682824466545189864L;
	private static final Logger LOG = LoggerFactory.getLogger(AccountItemLayout.class);

	private Button save;
	private Button update;
	private Button delete;
	
	private Notification notification;
	
	private AccountRepository repository;
	
	private RestClient restClient;

	private Account account;

	public AccountItemLayout(AccountRepository repository, RestClient client, Account account) {
		this.account = account;
		this.restClient = client;
		this.repository = repository;
		initLayout();
	}

	private void initLayout() {
		addIdField(account.getId());
		addNameField(account.getName());
		addButtons();
	}

	private void addNameField(String name) {
		TextField nameField = new TextField();
		nameField.setValue(name);
		addComponent(nameField);
	}

	private void addIdField(String id) {
		Label idField = new Label(id);
		idField.setValue(id);
		idField.setEnabled(false);
		addComponent(idField);
	}

	private void addButtons() {
		HorizontalLayout footer = new HorizontalLayout();
		save = new Button("Save");
		save.addStyleName(ValoTheme.BUTTON_PRIMARY);
		save.addClickListener(event -> {
			try {
				restClient.saveAccount(account);
				showNotification("Sucess", "<b>" + account + "</b> was saved successfully in Elasticsearch Server", Notification.Type.HUMANIZED_MESSAGE);
			} catch (Exception e) {
				LOG.error("Exception while saving " + account, e);
				showNotification(
						"Error", 
						"Failed to save <b>" + account + "</b> into <i>Elasticsearch server</i>", 
						Notification.Type.ERROR_MESSAGE
				);
			}
		});

		update = new Button("Update");
		update.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		update.addClickListener(event -> {
			try {
				ResponseEntity<Void> response = restClient.updateAccount(account);
				if (null != response && HttpStatus.NO_CONTENT == response.getStatusCode()) {
					AccountEntity entity = AccountBuildHelper.buildAccountEntity(account);
					repository.update(entity.getName(), entity.getDocSysId());
				}
				showNotification("Sucess", "<b>" + account + "</b> was updated successfully", Notification.Type.HUMANIZED_MESSAGE);
			} catch (Exception e) {
				LOG.error("Exception while updating " + account, e);
				showNotification(
						"Error", 
						"Failed to update <b>" + account + "</b> from <i>Elasticsearch server</i>", 
						Notification.Type.ERROR_MESSAGE
				);
			}
		});

		delete = new Button("Delete");
		delete.addStyleName(ValoTheme.BUTTON_DANGER);
		delete.setIcon(VaadinIcons.DEL);
		delete.addClickListener(event -> {
			try {
				restClient.removeAccount(account.getId());
				repository.delete(AccountBuildHelper.buildAccountEntity(account));
				showNotification("Sucess", "<b>" + account + "</b> was successfully removed from Elasticsearch server", Notification.Type.HUMANIZED_MESSAGE);
			} catch (Exception e) {
				LOG.error("Exception while deleting " + account, e);
				showNotification(
						"Error", 
						"Failed to delete <b>" + account + "</b> from <i>Elasticsearch server</i>", 
						Notification.Type.ERROR_MESSAGE
				);
			}
		});
		footer.addComponents(save, update, delete);
		addComponent(footer);
	}

	private void showNotification(String caption, String description, Notification.Type messageType) {
		notification = new Notification(caption, description, messageType, true);
		notification.show(Page.getCurrent());
	}
		
}
