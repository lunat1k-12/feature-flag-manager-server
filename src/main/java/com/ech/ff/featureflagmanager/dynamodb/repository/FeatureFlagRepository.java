package com.ech.ff.featureflagmanager.dynamodb.repository;

import com.ech.ff.featureflagmanager.dynamodb.entity.FeatureFlag;
import com.ech.ff.featureflagmanager.dynamodb.repository.base.DynamoDbRepository;
import com.ech.ff.featureflagmanager.security.dto.CognitoUser;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing feature flags in DynamoDB.
 */
@Slf4j
public class FeatureFlagRepository extends DynamoDbRepository<FeatureFlag> {

    /**
     * Constructs a new FeatureFlagRepository.
     *
     * @param dynamoDbTable The DynamoDB table for feature flags
     */
    public FeatureFlagRepository(DynamoDbTable<FeatureFlag> dynamoDbTable) {
        super(dynamoDbTable);
    }

    /**
     * Get a feature flag by name and environment.
     *
     * @param name The feature flag name
     * @param envName The environment name
     * @return The feature flag if found, otherwise empty
     */
    public Optional<FeatureFlag> getByName(String name, String envName) {
        log.info("Getting feature flag by name: {}, env: {}", name, envName);
        return getItem(envName, name);
    }

    /**
     * Get all feature flags for a specific environment.
     *
     * @param envName The environment name
     * @return List of feature flags for the environment
     */
    public List<FeatureFlag> getEnvFF(String envName, CognitoUser user) {
        log.info("Getting all feature flags for env: {}", envName);
        return queryByIndex("FFUserId", user.getId(), envName);
    }

    /**
     * Delete a feature flag.
     *
     * @param name The feature flag name
     * @param envName The environment name
     */
    public void deleteFF(String name, String envName) {
        log.info("Deleting feature flag name: {}, env: {}", name, envName);
        deleteItem(envName, name);
    }
}
