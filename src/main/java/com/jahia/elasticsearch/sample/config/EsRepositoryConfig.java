package com.jahia.elasticsearch.sample.config;

import com.jahia.elasticsearch.sample.security.EsYmlConfig;
import com.jahia.elasticsearch.sample.util.JahiaUtil;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * Configuration of elasticsearch for access to repository
 */
@Configuration
@Import(EsRestClientConfig.class)
public class EsRepositoryConfig extends AbstractFactoryBean<RestHighLevelClient> {
    private static final Logger LOG = LoggerFactory.getLogger(EsRepositoryConfig.class);

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    private RestHighLevelClient restHighLevelClient;
    
    @Nullable
    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    @NotNull
    protected RestHighLevelClient createInstance() throws Exception {
        EsYmlConfig esConfig = JahiaUtil.readYamlConfig();
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "elastic"));
        HttpHost httpHost = new HttpHost(esConfig.getServer(), esConfig.getPort(), esConfig.getProtocol());
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost).setHttpClientConfigCallback(
        		httpAsyncClientBuilder -> {
					httpAsyncClientBuilder.disableAuthCaching();				
					return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        		}
        );
        restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        return restHighLevelClient;
    }
        
    @Override
    public void destroy() {
        try {
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
        } catch (final Exception e) {
            LOG.error("Error closing ElasticSearch client: ", e);
        }
    }
}
