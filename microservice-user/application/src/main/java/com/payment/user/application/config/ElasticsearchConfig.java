package com.payment.user.application.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ElasticsearchConfig {

    @Value("${elastic.host}")
    private String host;
    @Value("${elastic.username}")
    private String username;
    @Value("${elastic.password}")
    private String password;

    private ElasticsearchClient esClient;

    @Bean
    public ElasticsearchClient ElasticConfig() {
        if (esClient == null) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

            RestClientBuilder builder = RestClient.builder(HttpHost.create(host)).setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
            RestClient restClient = builder.build();
            esClient = new ElasticsearchClient(new RestClientTransport(restClient, new JacksonJsonpMapper()));
        }

        log.info("Elastic client started {}", host);
        return esClient;
    }
}
