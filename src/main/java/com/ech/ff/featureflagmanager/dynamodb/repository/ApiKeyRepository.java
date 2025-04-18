package com.ech.ff.featureflagmanager.dynamodb.repository;

import com.ech.ff.featureflagmanager.dynamodb.entity.ApiKey;
import com.ech.ff.featureflagmanager.dynamodb.repository.base.DynamoDbRepository;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing API keys in DynamoDB.
 */
@Slf4j
public class ApiKeyRepository extends DynamoDbRepository<ApiKey> {

    /**
     * Constructs a new ApiKeyRepository.
     *
     * @param dynamoDbTable The DynamoDB table for API keys
     */
    public ApiKeyRepository(DynamoDbTable<ApiKey> dynamoDbTable) {
        super(dynamoDbTable);
    }

    /**
     * Get all API keys for a specific environment.
     *
     * @param envName The environment name
     * @return List of API keys for the environment
     */
    public List<ApiKey> getEnvKeys(String envName) {
        log.info("Getting all API keys for environment: {}", envName);
        return queryByPartitionKey(envName);
    }

    /**
     * Get a specific API key by key and environment name.
     *
     * @param key The API key
     * @param envName The environment name
     * @return The API key if found, otherwise empty
     */
    public Optional<ApiKey> getKey(String key, String envName) {
        log.info("Getting API key: {}, environment: {}", key, envName);
        return getItem(envName, key);
    }

    /**
     * Delete an API key.
     *
     * @param key The API key
     * @param envName The environment name
     */
    public void deleteKey(String key, String envName) {
        log.info("Deleting API key: {}, environment: {}", key, envName);
        deleteItem(envName, key);
    }
}
