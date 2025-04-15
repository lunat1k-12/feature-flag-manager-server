package com.ech.ff.featureflagmanager.dynamodb.repository;

import com.ech.ff.featureflagmanager.dynamodb.entity.ApiKey;
import com.ech.ff.featureflagmanager.dynamodb.entity.FeatureFlag;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;

@RequiredArgsConstructor
public class FeatureFlagRepository {

    private final DynamoDbTable<FeatureFlag> dynamoDbTable;

    public void save(FeatureFlag ff) {
        dynamoDbTable.putItem(ff);
    }

    public FeatureFlag getByName(String name, String envName) {
        return dynamoDbTable.getItem(FeatureFlag.builder()
                        .featureName(name)
                        .envName(envName)
                .build());
    }

    public List<FeatureFlag> getEnvFF(String envName) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(envName).build()
        );

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build();

        return dynamoDbTable.query(request).items().stream().toList();
    }

    public void deleteFF(String name, String envName) {
        dynamoDbTable.deleteItem(FeatureFlag.builder()
                        .featureName(name)
                        .envName(envName)
                .build());
    }
}
