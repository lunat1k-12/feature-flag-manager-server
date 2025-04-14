package com.ech.ff.featureflagmanager.dynamodb.repository;

import com.ech.ff.featureflagmanager.dynamodb.entity.ApiKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ApiKeyRepository {

    private final DynamoDbTable<ApiKey> dynamoDbTable;

    public void save(ApiKey apiKey) {
        log.info("Save new API key: {}", apiKey);
        dynamoDbTable.putItem(apiKey);
    }

    public List<ApiKey> getEnvKeys(String envName) {
        return null;
    }
}
