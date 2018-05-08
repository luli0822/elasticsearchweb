package com.jahia.elasticsearch.sample.business.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jahia.elasticsearch.sample.business.repository.AccountRepository;
import com.jahia.elasticsearch.sample.business.service.AccountService;
import com.jahia.elasticsearch.sample.rest.data.Account;
import java.util.Map;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;


/**
 * Service implementation class. Reference to {@link AccountRepository}
 */
@Service
public class AccountServiceImpl implements AccountService  {
    private static final String INDEX = "accounts";
    private static final String TYPE = "account";
    private ObjectMapper objectMapper;
    private RestHighLevelClient restHighLevelClient;
    
    public AccountServiceImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }
    
    @Override
    public ActionResponse getAllAccounts() throws Exception {
    	SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    	sourceBuilder.query(QueryBuilders.matchAllQuery());
    	SearchRequest request = new SearchRequest(INDEX);
    	request.types(TYPE);
    	request.source(sourceBuilder);
    	return restHighLevelClient.search(request);
    }

    @Override
	public ActionResponse findAccountById(String id) throws Exception {
        GetRequest request = new GetRequest(INDEX, TYPE, id);
        return restHighLevelClient.get(request);
    }

    @Override
    public ActionResponse updateAccount(Account account) throws Exception {
    	@SuppressWarnings("rawtypes")
    	Map dataMap = objectMapper.convertValue(account, Map.class);
    	UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(account.getId())).doc(dataMap);
        return restHighLevelClient.update(updateRequest);
    }

    @Override
    public ActionResponse saveAccount(Account account) throws Exception {
        @SuppressWarnings("rawtypes")
		Map dataMap = objectMapper.convertValue(account, Map.class);
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, String.valueOf(account.getId())).source(dataMap).opType(DocWriteRequest.OpType.CREATE);
        return restHighLevelClient.index(indexRequest);
    }

    @Override
    public ActionResponse removeAccount(String id) throws Exception {
    	DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
    	return restHighLevelClient.delete(deleteRequest);
    }
    
}
