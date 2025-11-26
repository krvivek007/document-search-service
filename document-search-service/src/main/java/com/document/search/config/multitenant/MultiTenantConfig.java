package com.document.search.config.multitenant;

import com.document.search.model.TenantConfig;
import com.document.search.repository.TenantConfigRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Component
public class MultiTenantConfig {

    private HashMap<String, TenantMongoClient> multiTenantConfig;
    private final TenantConfigRepository tenantConfigRepository;


    @PostConstruct
    public void multiTenantMongoConfig() {
        final List<TenantConfig> multiTenantList = tenantConfigRepository.findAll();
        multiTenantConfig = new HashMap<>();

        for (final TenantConfig multiTenant : multiTenantList) {
            final String connectionUri = multiTenant.getUri();
            MongoClient client;

            if (connectionUri != null)
                client = MongoClients.create(connectionUri);
            else
                throw new RuntimeException("config props missing");

            final String database = multiTenant.getDatabaseName();
            final TenantMongoClient tenantMongoClient = new TenantMongoClient(client, database);
            this.multiTenantConfig.put(multiTenant.getTenantId(), tenantMongoClient);
        }
    }

    @PreDestroy
    public void destroy() {
        multiTenantConfig.values().forEach(mongo -> mongo.getClient().close());
    }
}