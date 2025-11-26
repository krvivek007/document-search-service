package com.document.search.config.multitenant;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

public class MongoMultiTenantFactory extends SimpleMongoClientDatabaseFactory {
    private final MultiTenantConfig multiTenantConfig;

    public MongoMultiTenantFactory(MongoClient mongoClient,
                                   String defaultDatabase, MultiTenantConfig multiTenantConfig) {
        super(mongoClient, defaultDatabase);
        this.multiTenantConfig = multiTenantConfig;
    }


    public MongoDatabase getMongoDatabase() throws DataAccessException {
        final String tenant = TenantContext.getTenantId();
        if (tenant != null) {
            final TenantMongoClient tenantMongoClient = multiTenantConfig.getMultiTenantConfig().get(tenant);
            if (tenantMongoClient == null) {
                throw new RuntimeException("Tenant not found " + tenant);
            }
            return tenantMongoClient.getClient().getDatabase(tenantMongoClient.getDatabase());
        } else
            return getMongoClient().getDatabase(getDefaultDatabaseName());
    }
}
