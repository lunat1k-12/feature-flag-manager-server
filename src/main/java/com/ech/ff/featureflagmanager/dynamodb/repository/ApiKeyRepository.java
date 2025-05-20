package com.ech.ff.featureflagmanager.dynamodb.repository;

import com.ech.ff.featureflagmanager.dynamodb.entity.ApiKey;
import com.ech.ff.featureflagmanager.dynamodb.repository.base.DynamoDbRepository;
import com.ech.ff.featureflagmanager.security.dto.CognitoUser;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * @param partitionKey The environment name
     * @return List of API keys for the environment
     */
    public List<ApiKey> getEnvKeys(String partitionKey, CognitoUser cognitoUser) {
        log.info("Getting all API keys for environment: {}, user: {}", partitionKey, cognitoUser.getId());
//        return queryByPartitionKey(envName);
        log.info("Querying entities by partition key: {}", partitionKey);
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(
                    Key.builder().partitionValue(partitionKey).build()
            );

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .filterExpression(Expression.builder().expression("userId = :cognitoUserId")
                            .putExpressionValue(":cognitoUserId", AttributeValue.builder()
                                    .s(cognitoUser.getId())
                                    .build())
//                            .expressionValues(Map.of("cognitoUserId", AttributeValue.builder()
//                                            .s(cognitoUser.getId())
//                                    .build()))
                            .build())
                    .build();

            return dynamoDbTable.query(request).items().stream().collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error querying entities by partition key: {}", partitionKey, e);
            throw new RuntimeException("Failed to query entities by partition key", e);
        }
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
