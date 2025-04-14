package com.ech.ff.featureflagmanager.dynamodb.repository;

import com.ech.ff.featureflagmanager.dynamodb.entity.Environment;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.List;

@RequiredArgsConstructor
public class EnvironmentRepository {

    private final DynamoDbTable<Environment> dynamoDbTable;

    public List<Environment> getAllEnvironments() {
        return dynamoDbTable.scan().items().stream().toList();
    }

    public void saveEnvironment(Environment environment) {
        dynamoDbTable.putItem(environment);
    }
}
