package com.document.search.config.multitenant;

import com.mongodb.client.MongoClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TenantMongoClient {
    private MongoClient client;
    private String database;

}
