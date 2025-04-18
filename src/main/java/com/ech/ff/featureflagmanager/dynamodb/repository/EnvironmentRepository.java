package com.ech.ff.featureflagmanager.dynamodb.repository;

import com.ech.ff.featureflagmanager.dynamodb.entity.Environment;
import com.ech.ff.featureflagmanager.dynamodb.repository.base.DynamoDbRepository;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing environments in DynamoDB.
 */
@Slf4j
public class EnvironmentRepository extends DynamoDbRepository<Environment> {

    /**
     * Constructs a new EnvironmentRepository.
     *
     * @param dynamoDbTable The DynamoDB table for environments
     */
    public EnvironmentRepository(DynamoDbTable<Environment> dynamoDbTable) {
        super(dynamoDbTable);
    }

    /**
     * Get all environments.
     *
     * @return List of all environments
     */
    public List<Environment> getAllEnvironments() {
        log.info("Getting all environments");
        return scanAll();
    }

    /**
     * Save an environment.
     *
     * @param environment The environment to save
     * @return The saved environment
     */
    public Environment saveEnvironment(Environment environment) {
        log.info("Saving environment: {}", environment);
        return save(environment);
    }

    /**
     * Get an environment by name.
     *
     * @param name The environment name
     * @return The environment if found, otherwise empty
     */
    public Optional<Environment> getByName(String name) {
        log.info("Getting environment by name: {}", name);
        return getItem(name, null);
    }

    /**
     * Delete an environment.
     *
     * @param name The environment name
     */
    public void deleteEnvironment(String name) {
        log.info("Deleting environment: {}", name);
        deleteItem(name, null);
    }
}
