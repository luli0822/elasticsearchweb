package com.jahia.elasticsearch.sample.rest.response;

import org.elasticsearch.action.ActionResponse;
import org.springframework.http.ResponseEntity;

/**
 * Response that encapsulates {@link ActionResponse} and {@link ResponseEntity},
 * including error handling.
 *
 * @param <T> object returned from rest service
 */
public class SampleRsResponse<T> {
	private ActionResponse actionResponse;
	private ResponseEntity<T> responseEntity;
	private String message;
	
	public SampleRsResponse() {}

	public SampleRsResponse(ActionResponse actionResponse, String message) {
		this.actionResponse = actionResponse;
		this.message = message;
	}
	
	public SampleRsResponse(ActionResponse actionResponse) {
		this.actionResponse = actionResponse;
	}
	
	public ActionResponse getActionResponse() {
		return actionResponse;
	}
	public void setActionResponse(ActionResponse actionResponse) {
		this.actionResponse = actionResponse;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseEntity<T> getResponseEntity() {
		return responseEntity;
	}

	public void setResponseEntity(ResponseEntity<T> responseEntity) {
		this.responseEntity = responseEntity;
	}
}
